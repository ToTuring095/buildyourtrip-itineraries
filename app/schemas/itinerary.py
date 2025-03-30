from pydantic import BaseModel, Field
from datetime import datetime
from typing import List, Optional


class LocationBase(BaseModel):
    name: str
    lat: float = Field(..., ge=-90, le=90)
    lng: float = Field(..., ge=-180, le=180)
    description: Optional[str] = None
    photo_url: Optional[str] = None
    duration: Optional[int] = Field(None, ge=1, description="Duration in days")


class LocationCreate(LocationBase):
    pass


class LocationUpdate(LocationBase):
    name: Optional[str] = None
    lat: Optional[float] = Field(None, ge=-90, le=90)
    lng: Optional[float] = Field(None, ge=-180, le=180)


class Location(LocationBase):
    id: int
    created_at: datetime
    updated_at: datetime

    class Config:
        from_attributes = True


class ItineraryBase(BaseModel):
    title: str
    description: str
    start_date: datetime
    end_date: datetime


class ItineraryCreate(ItineraryBase):
    locations: List[LocationCreate]


class ItineraryUpdate(BaseModel):
    title: Optional[str] = None
    description: Optional[str] = None
    start_date: Optional[datetime] = None
    end_date: Optional[datetime] = None
    locations: Optional[List[LocationCreate]] = None


class Itinerary(ItineraryBase):
    id: int
    user_id: str
    locations: List[Location]
    created_at: datetime
    updated_at: datetime

    class Config:
        from_attributes = True


class ItineraryList(BaseModel):
    items: List[Itinerary]
    total: int 