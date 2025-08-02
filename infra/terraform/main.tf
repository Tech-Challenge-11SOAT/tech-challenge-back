terraform {
  required_providers {
    digitalocean = {
      source = "digitalocean/digitalocean"
      version = "2.59.0"
    }
  }

  backend "s3" {
    bucket       = "tech-challenge-soat11-terraform-state"
    key          = "terraform/state"
    region       = "us-east-1"
    use_lockfile = true
  }

}

provider "digitalocean" {
  token = var.do_token
}

resource "digitalocean_kubernetes_cluster" "k8s_cluster" {
  name   = "tech-challenge-cluster"
  region = "nyc2"
  version = "latest"

  node_pool {
    name       = "db-node-pool"
    size       = "s-2vcpu-2gb"
    node_count = 1

    taint {
      key    = "dedicated"
      value  = "db"
      effect = "NoSchedule"
    }
    
    labels = {
      node-role = "database"
    }

  }
}

resource "digitalocean_kubernetes_node_pool" "app_node_pool" {
  cluster_id = digitalocean_kubernetes_cluster.k8s_cluster.id
  
  name       = "app-node-pool"
  size       = "s-4vcpu-8gb"
  node_count = 2
  tags       = ["backend"]

}
