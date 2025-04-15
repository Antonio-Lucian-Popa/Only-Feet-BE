CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    is_creator BOOLEAN DEFAULT FALSE,
    profile_picture_url TEXT,
    bio TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- stripe_accounts table
CREATE TABLE stripe_accounts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    stripe_account_id TEXT NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_stripe_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- subscriptions table
CREATE TABLE subscriptions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    subscriber_id UUID NOT NULL,
    creator_id UUID NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    price_at_purchase NUMERIC NOT NULL CHECK (price_at_purchase >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sub_user FOREIGN KEY (subscriber_id) REFERENCES users(id),
    CONSTRAINT fk_sub_creator FOREIGN KEY (creator_id) REFERENCES users(id)
);

-- payments table
CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    content_id UUID,
    amount NUMERIC NOT NULL CHECK (amount >= 0),
    payment_method TEXT,
    type TEXT, -- 'subscription' | 'custom_content' | 'tip'
    status TEXT, -- 'pending' | 'succeeded' | 'failed'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pay_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- content table
CREATE TABLE content (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    creator_id UUID NOT NULL,
    title TEXT,
    description TEXT,
    media_type TEXT CHECK (media_type IN ('image', 'video')),
    media_url TEXT NOT NULL,
    visibility TEXT CHECK (visibility IN ('public', 'subscribers_only')),
    category TEXT,
    tags TEXT[],
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_content_creator FOREIGN KEY (creator_id) REFERENCES users(id)
);

-- likes table
CREATE TABLE likes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    content_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_like_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_like_content FOREIGN KEY (content_id) REFERENCES content(id),
    CONSTRAINT uq_like UNIQUE (user_id, content_id) -- un user like o singură dată
);

-- comments table
CREATE TABLE comments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    content_id UUID NOT NULL,
    text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_comment_content FOREIGN KEY (content_id) REFERENCES content(id)
);

-- ratings table
CREATE TABLE ratings (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    content_id UUID NOT NULL,
    score INT CHECK (score >= 1 AND score <= 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rating_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_rating_content FOREIGN KEY (content_id) REFERENCES content(id),
    CONSTRAINT uq_rating UNIQUE (user_id, content_id)
);

-- custom_requests table
CREATE TABLE custom_requests (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    creator_id UUID NOT NULL,
    request_text TEXT NOT NULL,
    offer_amount NUMERIC NOT NULL CHECK (offer_amount >= 0),
    status TEXT CHECK (status IN ('pending', 'accepted', 'rejected', 'delivered')) DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_req_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_req_creator FOREIGN KEY (creator_id) REFERENCES users(id)
);

-- polls table
CREATE TABLE polls (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    creator_id UUID NOT NULL,
    question TEXT NOT NULL,
    options TEXT[] NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    CONSTRAINT fk_poll_creator FOREIGN KEY (creator_id) REFERENCES users(id)
);

-- poll_votes table
CREATE TABLE poll_votes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    poll_id UUID NOT NULL,
    user_id UUID NOT NULL,
    selected_option TEXT,
    voted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_vote_poll FOREIGN KEY (poll_id) REFERENCES polls(id),
    CONSTRAINT fk_vote_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT uq_poll_vote UNIQUE (poll_id, user_id)
);
