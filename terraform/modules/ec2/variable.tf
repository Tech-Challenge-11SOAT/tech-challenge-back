variable "public_key" {
  description = "Public key for SSH access to the EC2 instance"
  type        = string

}

variable "app_subnet_id" {
  description = "Subnet ID where the EC2 instance will be launched"
  type        = string

}

variable "app_sg_id" {
  description = "Security group ID for the EC2 instance"
  type        = string

}