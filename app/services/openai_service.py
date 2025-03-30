from typing import Dict, Any
import openai
from app.core.config import get_settings

settings = get_settings()
openai.api_key = settings.OPENAI_API_KEY


class OpenAIService:
    @staticmethod
    async def generate_itinerary(prompt: str) -> Dict[str, Any]:
        try:
            response = await openai.ChatCompletion.acreate(
                model="gpt-4",
                messages=[
                    {
                        "role": "system",
                        "content": """Sei un esperto di viaggi che aiuta a creare itinerari dettagliati.
                        Genera un itinerario strutturato con le seguenti informazioni:
                        - Titolo del viaggio
                        - Descrizione generale
                        - Lista delle destinazioni con:
                          - Nome del luogo
                          - Descrizione
                          - Durata consigliata (in giorni)
                        Formatta la risposta in JSON."""
                    },
                    {
                        "role": "user",
                        "content": prompt
                    }
                ],
                temperature=0.7,
                max_tokens=1500
            )
            
            return {
                "success": True,
                "data": response.choices[0].message.content
            }
        
        except Exception as e:
            return {
                "success": False,
                "error": str(e)
            }

    @staticmethod
    def parse_itinerary_response(response: str) -> Dict[str, Any]:
        """
        Converte la risposta di OpenAI in un formato strutturato.
        Da implementare in base al formato della risposta.
        """
        # TODO: Implementare il parsing della risposta
        pass 