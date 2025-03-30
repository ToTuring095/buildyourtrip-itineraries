from sqlalchemy import Column, Integer, String, DateTime, Float, ForeignKey, Table
from sqlalchemy.orm import relationship
from datetime import datetime
from app.db.base import Base

# Tabella di associazione per la relazione many-to-many tra Itinerary e Location
itinerary_locations = Table(
    'itinerary_locations',
    Base.metadata,
    Column('itinerary_id', Integer, ForeignKey('itineraries.id'), primary_key=True),
    Column('location_id', Integer, ForeignKey('locations.id'), primary_key=True)
)

class Location(Base):
    __tablename__ = "locations"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, index=True)
    description = Column(String, nullable=True)
    lat = Column(Float, nullable=False)
    lng = Column(Float, nullable=False)
    photo_url = Column(String, nullable=True)
    duration = Column(Integer, nullable=True)  # Durata in giorni
    created_at = Column(DateTime, default=datetime.utcnow)
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)

class Itinerary(Base):
    __tablename__ = "itineraries"

    id = Column(Integer, primary_key=True, index=True)
    title = Column(String, index=True)
    description = Column(String)
    start_date = Column(DateTime, nullable=False)
    end_date = Column(DateTime, nullable=False)
    user_id = Column(String, nullable=False)
    created_at = Column(DateTime, default=datetime.utcnow)
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)

    # Relazione many-to-many con Location
    locations = relationship(
        "Location",
        secondary=itinerary_locations,
        backref="itineraries"
    ) 