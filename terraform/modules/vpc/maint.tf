resource "aws_vpc" "tech_vpc" {
  cidr_block = "10.0.0.0/16"
}

resource "aws_subnet" "tech_app_subnet" {
  vpc_id                  = aws_vpc.tech_vpc.id
  cidr_block              = "10.0.1.0/24"
  map_public_ip_on_launch = true

}

resource "aws_internet_gateway" "tech_igw" {
  vpc_id = aws_vpc.tech_vpc.id

}

resource "aws_route_table" "tech_route_table" {
  vpc_id = aws_vpc.tech_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.tech_igw.id
  }
}

resource "aws_route_table_association" "tech_route_table_association" {
  subnet_id      = aws_subnet.tech_app_subnet.id
  route_table_id = aws_route_table.tech_route_table.id
}


resource "aws_subnet" "tech_db_subnet_1" {
  vpc_id                  = aws_vpc.tech_vpc.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = "us-east-1a"
  map_public_ip_on_launch = false
}

resource "aws_subnet" "tech_db_subnet_2" {
  vpc_id                  = aws_vpc.tech_vpc.id
  cidr_block              = "10.0.3.0/24"
  availability_zone       = "us-east-1b"
  map_public_ip_on_launch = false
}