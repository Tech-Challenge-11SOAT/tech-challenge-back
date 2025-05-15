output "vpc_id" {
  value = aws_vpc.tech_vpc.id

}

output "vpc_cidr_block" {
  value = aws_vpc.tech_vpc.cidr_block

}

output "app_subnet_id" {
  value = aws_subnet.tech_app_subnet.id

}

output "db_subnet_id_1" {
  value = aws_subnet.tech_db_subnet_1.id

}

output "db_subnet_id_2" {
  value = aws_subnet.tech_db_subnet_2.id

}