from typing import List, Optional
from sqlalchemy.orm import Session
from app.models.itinerary import Itinerary, Location
from app.schemas.itinerary import ItineraryCreate, LocationCreate


class ItineraryRepository:
    def __init__(self, db: Session):
        self.db = db

    def get_itinerary(self, itinerary_id: int) -> Optional[Itinerary]:
        return self.db.query(Itinerary).filter(Itinerary.id == itinerary_id).first()

    def get_itineraries_by_user(self, user_id: str, skip: int = 0, limit: int = 10) -> List[Itinerary]:
        return (
            self.db.query(Itinerary)
            .filter(Itinerary.user_id == user_id)
            .offset(skip)
            .limit(limit)
            .all()
        )

    def create_itinerary(self, itinerary: ItineraryCreate, user_id: str) -> Itinerary:
        # Crea le locations
        locations = []
        for loc_data in itinerary.locations:
            location = Location(
                name=loc_data.name,
                description=loc_data.description,
                lat=loc_data.lat,
                lng=loc_data.lng,
                photo_url=loc_data.photo_url,
                duration=loc_data.duration
            )
            locations.append(location)
            self.db.add(location)
        
        # Crea l'itinerario
        db_itinerary = Itinerary(
            title=itinerary.title,
            description=itinerary.description,
            start_date=itinerary.start_date,
            end_date=itinerary.end_date,
            user_id=user_id,
            locations=locations
        )
        
        self.db.add(db_itinerary)
        self.db.commit()
        self.db.refresh(db_itinerary)
        return db_itinerary

    def update_itinerary(self, itinerary_id: int, itinerary_data: dict) -> Optional[Itinerary]:
        db_itinerary = self.get_itinerary(itinerary_id)
        if not db_itinerary:
            return None

        for key, value in itinerary_data.items():
            if hasattr(db_itinerary, key) and value is not None:
                setattr(db_itinerary, key, value)

        self.db.commit()
        self.db.refresh(db_itinerary)
        return db_itinerary

    def delete_itinerary(self, itinerary_id: int) -> bool:
        db_itinerary = self.get_itinerary(itinerary_id)
        if not db_itinerary:
            return False

        self.db.delete(db_itinerary)
        self.db.commit()
        return True 