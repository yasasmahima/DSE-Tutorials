package main

import (
	"fmt"
	"go-postgres/router"
	"log"
	"net/http"
)

func main() {
	r := Router.Router()
	fmt.Println("App starts on the port 8080...")
	log.Fatal(http.ListenAndServe(":8080", r))
}
