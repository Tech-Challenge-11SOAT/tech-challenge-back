variable "db_username" {
  description = "The username for the database"
  type        = string
  sensitive   = true

}

variable "db_password" {
  description = "The password for the database"
  type        = string
  sensitive   = true

}

variable "db_sg_id" {
  description = "Security group ID for the EC2 instance"
  type        = string

}

variable "db_subnet_id_1" {
  description = "Subnet ID where the EC2 instance will be launched"
  type        = string

}

variable "db_subnet_id_2" {
  description = "Subnet ID where the EC2 instance will be launched"
  type        = string

}