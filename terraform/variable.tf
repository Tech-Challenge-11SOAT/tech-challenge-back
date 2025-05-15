variable "public_key" {
  type        = string
  description = "Public key for SSH access to the EC2 instance"
}

variable "db_username" {
  type        = string
  description = "The username for the database"
  sensitive   = true
}

variable "db_password" {
  type        = string
  description = "The password for the database"
  sensitive   = true
}