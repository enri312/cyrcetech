@echo off
curl -s -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJDRU5WIiwiaWF0IjoxNzY1MDgwMzcwLCJleHAiOjE3NjUxNjY3NzB9.FUv81Gv5w5asx6pQog8XKMCvXEPgW8X6-HjCEZwoxfU" http://localhost:8080/api/customers > customers.json
