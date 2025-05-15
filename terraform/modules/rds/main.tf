
resource "aws_db_subnet_group" "tech_db_subnet_group" {
  name        = "tech-db-subnet-group"
  subnet_ids  = [var.db_subnet_id_1, var.db_subnet_id_2]
  description = "Subnet group for the RDS instance"
}

resource "aws_db_instance" "tech_db" {
  db_name                = "techdb" 
  allocated_storage      = 20
  engine                 = "mysql"
  engine_version         = "8.0"
  instance_class         = "db.t3.micro"
  username               = var.db_username
  password               = var.db_password
  skip_final_snapshot    = true
  publicly_accessible    = false
  vpc_security_group_ids = [var.db_sg_id]
  db_subnet_group_name   = aws_db_subnet_group.tech_db_subnet_group.name

}