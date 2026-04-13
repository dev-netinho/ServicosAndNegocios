package com.deefy.group2.dto.response;

//DTO para response ao login
public record LoginResponse(
        String token,
        String type,
        String username)
{
}
