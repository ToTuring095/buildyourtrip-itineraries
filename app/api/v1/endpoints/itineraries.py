from typing import List, Optional
from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session
from app.db.session import get_db
from app.repositories.itinerary_repository import ItineraryRepository
from app.services import ai_client
from app.schemas.itinerary import (
    Itinerary,
    ItineraryCreate,
    ItineraryUpdate,
    ItineraryList
)

router = APIRouter()


@router.post("/generate", response_model=dict)
async def generate_itinerary(
    prompt: str,
    db: Session = Depends(get_db)
):
    """
    Genera un itinerario utilizzando l'AI
    """
    # Chiama il servizio AI per generare l'itinerario
    response = await ai_client.generate_itinerary(prompt)
    
    if not response["success"]:
        raise HTTPException(status_code=500, detail=response["error"])
    
    return response


@router.post("/optimize/{itinerary_id}", response_model=dict)
async def optimize_itinerary(
    itinerary_id: int,
    days: int,
    db: Session = Depends(get_db)
):
    """
    Ottimizza un itinerario esistente
    """
    repository = ItineraryRepository(db)
    itinerary = repository.get_itinerary(itinerary_id)
    if not itinerary:
        raise HTTPException(status_code=404, detail="Itinerario non trovato")

    # Prepara i dati delle locations per l'ottimizzazione
    locations = [
        {
            "name": loc.name,
            "description": loc.description,
            "duration": loc.duration
        }
        for loc in itinerary.locations
    ]

    # Chiama il servizio AI per ottimizzare l'itinerario
    response = await ai_client.optimize_itinerary(locations, days)
    
    if not response["success"]:
        raise HTTPException(status_code=500, detail=response["error"])
    
    return response


@router.post("/activities/{location_id}", response_model=dict)
async def get_activities(
    location_id: int,
    duration: int,
    db: Session = Depends(get_db)
):
    """
    Ottiene suggerimenti di attività per una location
    """
    repository = ItineraryRepository(db)
    location = repository.get_location(location_id)
    if not location:
        raise HTTPException(status_code=404, detail="Location non trovata")

    # Chiama il servizio AI per suggerire attività
    response = await ai_client.suggest_activities(location.name, duration)
    
    if not response["success"]:
        raise HTTPException(status_code=500, detail=response["error"])
    
    return response


@router.get("/{user_id}", response_model=ItineraryList)
def get_user_itineraries(
    user_id: str,
    skip: int = Query(0, ge=0),
    limit: int = Query(10, ge=1, le=100),
    db: Session = Depends(get_db)
):
    """
    Recupera gli itinerari di un utente
    """
    repository = ItineraryRepository(db)
    itineraries = repository.get_itineraries_by_user(user_id, skip, limit)
    total = len(itineraries)  # In produzione, fare una query COUNT separata
    return {"items": itineraries, "total": total}


@router.post("", response_model=Itinerary)
def create_itinerary(
    itinerary: ItineraryCreate,
    user_id: str,
    db: Session = Depends(get_db)
):
    """
    Crea un nuovo itinerario
    """
    repository = ItineraryRepository(db)
    return repository.create_itinerary(itinerary, user_id)


@router.get("/{itinerary_id}", response_model=Itinerary)
def get_itinerary(
    itinerary_id: int,
    db: Session = Depends(get_db)
):
    """
    Recupera un itinerario specifico
    """
    repository = ItineraryRepository(db)
    itinerary = repository.get_itinerary(itinerary_id)
    if not itinerary:
        raise HTTPException(status_code=404, detail="Itinerario non trovato")
    return itinerary


@router.put("/{itinerary_id}", response_model=Itinerary)
def update_itinerary(
    itinerary_id: int,
    itinerary: ItineraryUpdate,
    db: Session = Depends(get_db)
):
    """
    Aggiorna un itinerario esistente
    """
    repository = ItineraryRepository(db)
    updated_itinerary = repository.update_itinerary(itinerary_id, itinerary.dict(exclude_unset=True))
    if not updated_itinerary:
        raise HTTPException(status_code=404, detail="Itinerario non trovato")
    return updated_itinerary


@router.delete("/{itinerary_id}")
def delete_itinerary(
    itinerary_id: int,
    db: Session = Depends(get_db)
):
    """
    Elimina un itinerario
    """
    repository = ItineraryRepository(db)
    if not repository.delete_itinerary(itinerary_id):
        raise HTTPException(status_code=404, detail="Itinerario non trovato")
    return {"message": "Itinerario eliminato con successo"} 