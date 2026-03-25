# Final Gap Fix - Item B

You are a senior Java backend engineer.

Implement ONLY this missing item:

## Item
Add order edit endpoint for PENDING orders.

## Required behavior
- Add endpoint: `PUT /orders/{id}`
- Allow updating only these fields:
    - customerName
    - phone
    - email
    - address
    - note
- Only orders in `PENDING` status can be edited
- Do NOT allow editing processed orders
- Follow architecture in `architecture.txt`
- Keep controller thin
- Use DTO + service + repository pattern
- Do not change database schema
- Do not use Python
- Do not re-read large docs unnecessarily
- Use existing codebase and current structure only

## Required output
1. request DTO
2. service interface update
3. service implementation
4. controller endpoint
5. validation and business rule checks
6. summary of changed files only