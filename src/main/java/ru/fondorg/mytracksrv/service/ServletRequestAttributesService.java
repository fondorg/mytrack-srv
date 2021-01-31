package ru.fondorg.mytracksrv.service;

import ru.fondorg.mytracksrv.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
 * Fetches data from HttpServletRequest
 */
public interface ServletRequestAttributesService {

    /**
     * Get user data from the provided servlet request
     * @param request request to fetch data from
     * @return User instance
     */
    public User getUserFromRequest(HttpServletRequest request);
}
