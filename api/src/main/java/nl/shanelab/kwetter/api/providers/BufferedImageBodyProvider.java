package nl.shanelab.kwetter.api.providers;

import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Base64;

@Provider
@Produces({"image/png", "image/jpg"})
@Consumes({"image/png", "image/jpg"})
public class BufferedImageBodyProvider  implements MessageBodyWriter<BufferedImage>, MessageBodyReader<BufferedImage> {

    @Override
    public boolean isWriteable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
        return type == BufferedImage.class;
    }

    @Override
    public long getSize(BufferedImage t, Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
        return -1;
    }

    @Override
    public void writeTo(BufferedImage image, Class<?> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, Object> mm, OutputStream out) throws IOException, WebApplicationException {
        ImageIO.write(image, mt.getSubtype(), out);
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == BufferedImage.class;
    }

    @Override
    public BufferedImage readFrom(Class<BufferedImage> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        String payload = new String(IOUtils.toByteArray(entityStream));

        int pos = payload.indexOf(",") + 1;
        payload = payload.substring(pos, payload.length());

        byte[] bytes = Base64.getDecoder().decode(payload);

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        return image;
    }
}
