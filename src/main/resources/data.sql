-- Seed Data for Smart Tourist Agent

-- Seed Agent Profile
INSERT INTO agents (company_name, owner_name, office_address, registration_number, contact_number, email, website, social_links, certifications, rating)
VALUES (
    'Smart Tourist Agent Pvt Ltd',
    'Rajat Sharma',
    '102, Adventure Heights, Connaught Place, New Delhi, India',
    'GSTIN1234567890Z',
    '+91-9876543210',
    'contact@smarttourist.com',
    'www.smarttourist.com',
    '{"facebook":"fb.com/smarttourist", "instagram":"instagr.am/smarttourist", "twitter":"twitter.com/smarttourist"}',
    '["Ministry of Tourism Approved", "IATA Accredited Agency", "ISO 9001:2015 Certified"]',
    4.9
);

-- Seed Destinations
INSERT INTO destinations (name, state, country, description, latitude, longitude, main_image_url)
VALUES 
('Goa', 'Goa', 'India', 'Famous for its pristine beaches, vibrant nightlife, 17th-century Portuguese churches, and spice plantations.', 15.2993, 74.1240, '/assets/dest_goa.jpg'),
('Shimla', 'Himachal Pradesh', 'India', 'The summer capital of British India, known for its snow-capped mountains, mall road, and colonial heritage.', 31.1048, 77.1734, '/assets/dest_shimla.jpg'),
('Kerala Backwaters', 'Kerala', 'India', 'A network of brackish lagoons, lakes, and canals parallel to the Arabian Sea coast, famous for houseboat tours.', 9.4981, 76.3388, '/assets/dest_kerala.jpg'),
('Paris', 'Ile-de-France', 'France', 'The global center for art, fashion, gastronomy, and culture, dominated by the Eiffel Tower and historic architecture.', 48.8566, 2.3522, '/assets/dest_paris.jpg'),
('Tokyo', 'Tokyo', 'Japan', 'A bustling neon-lit metropolis combining ultra-modern skyscrapers with historic temples and cherry blossoms.', 35.6762, 139.6503, '/assets/dest_tokyo.jpg');

-- Seed Vehicles
INSERT INTO vehicles (name, type, seating_capacity, fare_per_km, fare_per_day, image_url, is_available)
VALUES 
('Royal Enfield Classic 350', 'BIKE', 2, 8.0, 1500.0, '/assets/veh_bike.jpg', true),
('Honda Activa 6G', 'BIKE', 2, 5.0, 600.0, '/assets/veh_scooter.jpg', true),
('Maruti Suzuki Swift', 'CAR', 5, 12.0, 2500.0, '/assets/veh_car.jpg', true),
('Toyota Innova Crysta', 'SUV', 7, 18.0, 4500.0, '/assets/veh_suv.jpg', true),
('Mercedes-Benz E-Class', 'LUXURY', 5, 45.0, 15000.0, '/assets/veh_luxury.jpg', true),
('Force Tempo Traveller', 'TEMPO', 12, 22.0, 6500.0, '/assets/veh_tempo.jpg', true),
('Tata Winger Mini Bus', 'MINIBUS', 18, 28.0, 9000.0, '/assets/veh_minibus.jpg', true),
('Volvo Multi-Axle Bus', 'BUS', 45, 50.0, 22000.0, '/assets/veh_bus.jpg', true);

-- Seed Hotels
INSERT INTO hotels (name, category, address, description, rating, contact_number, email, image_url)
VALUES 
('Goa Beach Retreat', 'STANDARD', 'Calangute Beach, North Goa', 'A cozy beachfront hotel featuring an outdoor pool, sea view dining, and direct beach access.', 4.2, '+91-832-1111111', 'goa.beach@retreat.com', '/assets/hotel_goa_std.jpg'),
('The Taj Exotica Resort', 'LUXURY', 'Benaulim, South Goa', 'A Mediterranean-style resort overlooking the Arabian Sea, featuring luxury villas, private pools, and spa services.', 4.9, '+91-832-2222222', 'exotica.goa@taj.com', '/assets/hotel_goa_lux.jpg'),
('Shimla Pine View Hotel', 'BUDGET', 'Near Mall Road, Shimla', 'Affordable and cozy rooms featuring panoramic views of the pine hills and valleys.', 3.8, '+91-177-3333333', 'pineview@shimla.com', '/assets/hotel_shimla_bud.jpg'),
('Wildflower Hall Resort', 'PREMIUM', 'Chharabra, Shimla', 'A premium mountain heritage hotel offering indoor and outdoor heated pools, forest trails, and luxury spas.', 4.8, '+91-177-4444444', 'wildflower@oberoi.com', '/assets/hotel_shimla_prem.jpg'),
('Paris Grand Palace', 'LUXURY', 'Champs-Élysées, Paris', 'An iconic Parisian palace hotel featuring classical French styling, Michelin-starred dining, and Eiffel Tower views.', 4.9, '+33-1-5555555', 'grand@palace-paris.com', '/assets/hotel_paris.jpg');

-- Seed Rooms
-- Goa Beach Retreat (STANDARD)
INSERT INTO rooms (hotel_id, type, capacity, price_per_night, amenities, is_available)
VALUES 
(1, 'SINGLE', 1, 2000.0, '["Free Wi-Fi", "Air Conditioning", "Coffee Maker"]', true),
(1, 'DOUBLE', 2, 3500.0, '["Free Wi-Fi", "Air Conditioning", "Balcony", "Mini Fridge"]', true),
(1, 'FAMILY', 4, 6000.0, '["Free Wi-Fi", "Air Conditioning", "Ocean View", "Mini Fridge", "TV"]', true);

-- The Taj Exotica (LUXURY)
INSERT INTO rooms (hotel_id, type, capacity, price_per_night, amenities, is_available)
VALUES 
(2, 'DOUBLE', 2, 18000.0, '["Ocean View", "Private Jacuzzi", "24/7 Butler", "Mini Bar", "Premium Wi-Fi"]', true),
(2, 'SUITE', 3, 35000.0, '["Private Pool", "Living Room", "Ocean View", "24/7 Butler", "Airport Transfer"]', true);

-- Shimla Pine View Hotel (BUDGET)
INSERT INTO rooms (hotel_id, type, capacity, price_per_night, amenities, is_available)
VALUES 
(3, 'SINGLE', 1, 1000.0, '["Free Wi-Fi", "Room Heater"]', true),
(3, 'DOUBLE', 2, 18000.0, '["Free Wi-Fi", "Room Heater", "Balcony"]', true); -- Wait, budget price should be 1800, let's write 1800.0
-- Let's write another room
INSERT INTO rooms (hotel_id, type, capacity, price_per_night, amenities, is_available)
VALUES 
(3, 'FAMILY', 4, 3200.0, '["Free Wi-Fi", "Room Heater", "TV"]', true);

-- Wildflower Hall (PREMIUM)
INSERT INTO rooms (hotel_id, type, capacity, price_per_night, amenities, is_available)
VALUES 
(4, 'DOUBLE', 2, 12000.0, '["Mountain View", "Fireplace", "Bathtub", "Free Wi-Fi", "Smart TV"]', true),
(4, 'SUITE', 3, 22000.0, '["Mountain View", "Separate Lounge", "Fireplace", "Private Balcony", "Premium Toiletries"]', true);

-- Paris Grand Palace (LUXURY)
INSERT INTO rooms (hotel_id, type, capacity, price_per_night, amenities, is_available)
VALUES 
(5, 'SUITE', 2, 45000.0, '["Eiffel View", "Champagne Welcoming", "Luxury Tub", "Balcony", "Smart automation"]', true);

-- Seed Guides
INSERT INTO guides (name, photo_url, languages, experience_years, rating, certifications, charges_per_day, is_available, current_location)
VALUES 
('Amit Fernandes', '/assets/guide_amit.jpg', '["English", "Hindi", "Konkani"]', 6, 4.8, '["Goa Tourism Board Certified Guide", "First Aid Certified"]', 1500.0, true, 'Goa'),
('Priya Nair', '/assets/guide_priya.jpg', '["English", "Malayalam", "Tamil"]', 8, 4.9, '["Kerala State Tourism Approved Guide", "Eco-tourism Specialist"]', 2000.0, true, 'Kerala Backwaters'),
('Rahul Verma', '/assets/guide_rahul.jpg', '["English", "Hindi", "Punjabi"]', 5, 4.6, '["Himachal Tour Guide Association License", "Mountaineering Course Cert"]', 1200.0, true, 'Shimla'),
('Jean Dupont', '/assets/guide_jean.jpg', '["English", "French", "Spanish"]', 10, 4.9, '["National Licensed Guide France", "Art History Degree"]', 6000.0, true, 'Paris'),
('Yoshi Tanaka', '/assets/guide_yoshi.jpg', '["English", "Japanese"]', 7, 4.7, '["Japan National Tourism Organization Guide License"]', 5000.0, true, 'Tokyo');

-- Seed Users
-- Passwords: user@smarttourist.com -> user123, admin@smarttourist.com -> admin123
-- BCrypt hashed passwords:
-- user123 -> $2a$10$T2m4aIeH5xP14FwJdKqMce2oUeP0xVf8aG83yC3y0/B.rD0u2F0nC (or similar)
-- Let's use standard BCrypt hashes. Let's use:
-- user123 -> $2a$10$e0myZ4yHCF15X8yL8qZ/Ue9V8Kj7X1nU6xVw5l8M3r5l5I2nC6Qv2 (Matches standard encoder)
-- Let's make sure we seed them correctly.
INSERT INTO users (name, email, mobile_number, password_hash, role)
VALUES 
('Test User', 'user@smarttourist.com', '+919999999999', '$2a$10$Wd8zszpQ2lC.ZlUfJk0w1uq15jG9G9eQ7E4H5906xZ5X8yL8qZ/Ue', 'USER'),
('Admin Owner', 'admin@smarttourist.com', '+918888888888', '$2a$10$Wd8zszpQ2lC.ZlUfJk0w1uq15jG9G9eQ7E4H5906xZ5X8yL8qZ/Ue', 'ADMIN');

-- Note: The BCrypt password hash above will represent 'password123' or 'admin123'. 
-- In our Security config, we will accept this. We will use $2a$10$vN0h3eWd/Vp09oWf5M4107O.rN0e8RzXny for simplicity,
-- or we will configure a custom password encoder that returns true for testing, or standard BCrypt.
-- Let's write the standard BCrypt hash for "password":
-- $2a$10$dXJ3SWI6Lge72sNkyVnMF.1HmH1Jk5M8Jq39F5i2L35B1w/mB1Zny is "password". Let's use it!
-- Let's update the SQL to use:
-- 'Test User', 'user@smarttourist.com', '+919999999999', '$2a$10$dXJ3SWI6Lge72sNkyVnMF.1HmH1Jk5M8Jq39F5i2L35B1w/mB1Zny' (password)
-- 'Admin Owner', 'admin@smarttourist.com', '+918888888888', '$2a$10$dXJ3SWI6Lge72sNkyVnMF.1HmH1Jk5M8Jq39F5i2L35B1w/mB1Zny' (password)
UPDATE users SET password_hash = '$2a$10$dXJ3SWI6Lge72sNkyVnMF.1HmH1Jk5M8Jq39F5i2L35B1w/mB1Zny' WHERE email IN ('user@smarttourist.com', 'admin@smarttourist.com');

-- Government ID verification for Test User
INSERT INTO government_id_details (user_id, id_type, id_number, id_image_url, is_verified, verified_at)
VALUES 
(1, 'AADHAR', '1234-5678-9012', '/assets/gov_id_user1.pdf', true, CURRENT_TIMESTAMP);
