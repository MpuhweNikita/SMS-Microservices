CREATE TABLE IF NOT EXISTS courses (
    id SERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    credits INT NOT NULL
);
