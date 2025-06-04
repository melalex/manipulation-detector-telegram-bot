FROM python:3.13-slim

# Set workdir
WORKDIR /app

# Install system packages
RUN apt-get update && apt-get install -y \
    git \
    make \
    && rm -rf /var/lib/apt/lists/*

# Copy project files (including Makefile and service code)
COPY . .

# Pre-download model to avoid cold start
RUN make setup

# Default entrypoint
CMD ["make", "run"]
