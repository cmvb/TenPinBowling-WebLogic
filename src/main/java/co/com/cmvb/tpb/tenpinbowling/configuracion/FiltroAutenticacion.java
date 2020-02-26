package co.com.cmvb.tpb.tenpinbowling.configuracion;

import co.com.cmvb.tpb.tenpinbowling.constantes.DireccionesWeb;
import co.com.cmvb.tpb.tenpinbowling.mb.SesionMB;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.faces.application.ResourceHandler;

public class FiltroAutenticacion implements Filter {

    private FilterConfig filterConfig;
    private static final Logger LOG = Logger.getLogger(FiltroAutenticacion.class.getName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        res.addHeader("X-Frame-Options", "DENY");

        SesionMB sesionMB = (SesionMB) req.getSession().getAttribute(SesionMB.NOMBRE_LLAVE_TOKEN);
        String path = ((HttpServletRequest) request).getRequestURI();
        LOG.log(Level.INFO, "|°°°°|                                                                                              |°°°°|");
        LOG.log(Level.INFO, "|--->| PATH TPB:| {0}", path);
        LOG.log(Level.INFO, "|--->| -------------------------------------------------------------------------------------------- |<---|");
        LOG.log(Level.INFO, "|°°°°|                                                                                              |°°°°|");

        if (!path.startsWith(req.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER)) {
            if (sesionMB != null && sesionMB.getSesion() != null) {
                if (path.startsWith(req.getContextPath() + "/pages/")) {
                    chain.doFilter(req, res);
                } else {
                    res.sendRedirect(req.getContextPath() + DireccionesWeb.LOGIN);
                }
            } else {
                if (path.contains("error")) {
                    chain.doFilter(req, res);
                } else {
                    res.sendRedirect(req.getContextPath() + DireccionesWeb.LOGIN);
                }
            }
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        this.filterConfig = config;
    }

    @Override
    public void destroy() {
        filterConfig = null;
    }
}
