package ch.unine.vauchers.erasuretester;

import ch.unine.vauchers.erasuretester.backend.RedisStorageBackend;
import ch.unine.vauchers.erasuretester.backend.StorageBackend;
import ch.unine.vauchers.erasuretester.erasure.FileEncoderDecoder;
import ch.unine.vauchers.erasuretester.erasure.codes.ErasureCode;
import ch.unine.vauchers.erasuretester.erasure.codes.ReedSolomonCode;
import ch.unine.vauchers.erasuretester.frontend.FuseMemoryFrontend;
import net.fusejna.FuseException;

import java.io.IOException;

public class Main {

    public static void main(String[] argv) throws FuseException {
        // Disable logging completely for faster performance
        // Utils.disableLogging();

        ErasureCode erasureCode = new ReedSolomonCode(10, 4);
        StorageBackend storageBackend = new RedisStorageBackend();
        FileEncoderDecoder encdec = new FileEncoderDecoder(erasureCode, storageBackend);

        final FuseMemoryFrontend fuse = new FuseMemoryFrontend(encdec, false);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    storageBackend.disconnect();
                    fuse.unmount();
                } catch (IOException | FuseException e) {
                    e.printStackTrace();
                }
            }
        });

        fuse.mount(argv[0]);
    }

}
