from typing import Dict, Any, List
import httpx
from app.core.config import get_settings

settings = get_settings()
AI_SERVICE_URL = "http://localhost:8084/api/v1/generate"


async def generate_itinerary(prompt: str) -> Dict[str, Any]:
    """
    Chiama il servizio AI per generare un itinerario
    """
    async with httpx.AsyncClient() as client:
        try:
            response = await client.post(
                f"{AI_SERVICE_URL}/itinerary",
                params={"prompt": prompt}
            )
            response.raise_for_status()
            return response.json()
        except httpx.HTTPError as e:
            return {
                "success": False,
                "error": f"Errore nella chiamata al servizio AI: {str(e)}"
            }


async def optimize_itinerary(locations: List[Dict[str, Any]], days: int) -> Dict[str, Any]:
    """
    Chiama il servizio AI per ottimizzare un itinerario
    """
    async with httpx.AsyncClient() as client:
        try:
            response = await client.post(
                f"{AI_SERVICE_URL}/optimize",
                json={"locations": locations, "days": days}
            )
            response.raise_for_status()
            return response.json()
        except httpx.HTTPError as e:
            return {
                "success": False,
                "error": f"Errore nella chiamata al servizio AI: {str(e)}"
            }


async def suggest_activities(location: str, duration: int) -> Dict[str, Any]:
    """
    Chiama il servizio AI per suggerire attività
    """
    async with httpx.AsyncClient() as client:
        try:
            response = await client.post(
                f"{AI_SERVICE_URL}/activities",
                json={"location": location, "duration": duration}
            )
            response.raise_for_status()
            return response.json()
        except httpx.HTTPError as e:
            return {
                "success": False,
                "error": f"Errore nella chiamata al servizio AI: {str(e)}"
            } 