

CREATE TABLE users (
    user_id INT IDENTITY(1,1) PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL,
    CONSTRAINT chk_users_role
        CHECK (role IN ('employee', 'technician', 'admin'))
);

CREATE TABLE categories (
    category_id INT IDENTITY(1,1) PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE tickets (
    ticket_id INT IDENTITY(1,1) PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(MAX) NOT NULL,
    status VARCHAR(20) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    created_by INT NOT NULL,
    assigned_to INT NULL,
    category_id INT NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),

    CONSTRAINT chk_tickets_status
        CHECK (status IN ('open', 'in_progress', 'closed')),

    CONSTRAINT chk_tickets_priority
        CHECK (priority IN ('low', 'medium', 'high')),

    CONSTRAINT fk_tickets_created_by
        FOREIGN KEY (created_by) REFERENCES users(user_id),

    CONSTRAINT fk_tickets_assigned_to
        FOREIGN KEY (assigned_to) REFERENCES users(user_id),

    CONSTRAINT fk_tickets_category
        FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

CREATE TABLE ticket_updates (
    update_id INT IDENTITY(1,1) PRIMARY KEY,
    ticket_id INT NOT NULL,
    updated_by INT NOT NULL,
    update_note VARCHAR(MAX) NOT NULL,
    updated_at DATETIME DEFAULT GETDATE(),

    CONSTRAINT fk_ticket_updates_ticket
        FOREIGN KEY (ticket_id) REFERENCES tickets(ticket_id) ON DELETE CASCADE,

    CONSTRAINT fk_ticket_updates_updated_by
        FOREIGN KEY (updated_by) REFERENCES users(user_id)
);
