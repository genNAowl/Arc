package io.anuke.arc.backends.teavm;

import io.anuke.arc.*;
import io.anuke.arc.collection.*;
import io.anuke.arc.function.*;
import org.teavm.jso.ajax.*;
import org.teavm.jso.browser.*;

import java.io.*;

public class TeaVMNet implements Net{

    @Override
    public void http(HttpRequest httpRequest, Consumer<HttpResponse> success, Consumer<Throwable> failure){
        XMLHttpRequest req = XMLHttpRequest.create();
        req.open(httpRequest.method.toString(), httpRequest.url);
        httpRequest.headers.each(req::setRequestHeader);
        req.setOnReadyStateChange(() -> {
            if (req.getReadyState() != XMLHttpRequest.DONE) {
                return;
            }

            int statusGroup = req.getStatus() / 100;
            if (statusGroup != 2 && statusGroup != 3) {
                failure.accept(new IOException("HTTP status: " +
                req.getStatus() + " " + req.getStatusText()));
            } else {
                success.accept(new HttpResponse(){
                    @Override
                    public byte[] getResult(){
                        throw new UnsupportedOperationException("Not implemented");
                    }

                    @Override
                    public String getResultAsString(){
                        return req.getResponseText();
                    }

                    @Override
                    public InputStream getResultAsStream(){
                        throw new UnsupportedOperationException("Not implemented");
                    }

                    @Override
                    public HttpStatus getStatus(){
                        return HttpStatus.byCode(req.getStatus());
                    }

                    @Override
                    public String getHeader(String name){
                        return req.getResponseHeader(name);
                    }

                    @Override
                    public ObjectMap<String, Array<String>> getHeaders(){
                        throw new UnsupportedOperationException("Not implemented");
                    }
                });
            }
        });
        req.send();
    }

    @Override
    public boolean openURI(String URI) {
        Window.current().open(URI, "_blank");
        return true;
    }
}
