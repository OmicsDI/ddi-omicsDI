package uk.ac.ebi.ddi.ws.modules.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The object of this class is to check the upload location exists since this is not the case when in production.
 * Probably due to the versions of spring boot and tomcat, or perhaps due to it being deployed as a war file in a standalone tomcat.
 * <p>
 * Via an interceptor, it checks if the temporary upload location exists for every request and creates it if necessary.
 *  Might want to delete this in the future after updating to a later version of spring boot or tomcat.
 *  Or when migrated to spring boot's embedded tomcat.
 */
@Configuration
@ConditionalOnProperty("spring.servlet.multipart.enabled")
public class UploadLocationConfiguration implements WebMvcConfigurer {
    final String expectedUploadLocation;
    final ServletContext servletContext;
    public UploadLocationConfiguration(
            @Value("${spring.servlet.multipart.location}")
            String expectedUploadLocation,
            ServletContext servletContext) {
        this.expectedUploadLocation = expectedUploadLocation;
        this.servletContext = servletContext;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        var interceptor = new EnsureUploadLocationExistsInterceptor();
        registry.addInterceptor(interceptor)
                .addPathPatterns("/dataset/checkfile");
    }

    private class EnsureUploadLocationExistsInterceptor implements HandlerInterceptor {
        private static final Logger LOGGER = LoggerFactory.getLogger(EnsureUploadLocationExistsInterceptor.class);
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            Path path = null;
            if (StringUtils.isNotBlank(expectedUploadLocation)) {
                path = Path.of(servletContext.getRealPath(expectedUploadLocation));
            }
            if (path == null) {
                LOGGER.error("Could not get multipart temporary location from properties!");
                return false;
            }
            if (!Files.isDirectory(path)) {
                Files.createDirectories(path);
                LOGGER.info("Created temporary folder for multipart file uploads at {}", path);
            }
            return true;
        }
    }
}


