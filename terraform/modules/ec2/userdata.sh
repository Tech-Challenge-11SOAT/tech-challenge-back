#!/bin/bash
yum update -y
yum install -y docker 
yum install -y git

# Start Docker service
service docker start

# Add ec2-user to the docker group, so it can run Docker commands without sudo
usermod -aG docker ec2-user

# Install Docker Compose
curl -L "https://github.com/docker/compose/releases/download/v2.24.7/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
