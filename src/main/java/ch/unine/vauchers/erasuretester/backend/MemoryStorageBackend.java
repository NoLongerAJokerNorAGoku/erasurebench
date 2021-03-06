package ch.unine.vauchers.erasuretester.backend;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Storage backend implementation backed by a plain old Java Map object.
 */
public class MemoryStorageBackend extends StorageBackend {
    protected Map<Integer, String> blocksStorage;
    protected Map<String, FileMetadata> metadataStorage;

    public MemoryStorageBackend() {
        blocksStorage = new HashMap<>();
        metadataStorage = new HashMap<>();
    }

    @Override
    public Optional<FileMetadata> getFileMetadata(@NotNull String path) {
        return Optional.ofNullable(metadataStorage.get(path));
    }

    @Override
    public void setFileMetadata(@NotNull String path, @NotNull FileMetadata metadata) {
        metadataStorage.put(path, metadata);
    }

    @Override
    public Collection<String> getAllFilePaths() {
        return new ArrayList<>(metadataStorage.keySet());
    }

    @Override
    public Optional<String> retrieveAggregatedBlocks(int key) {
        return Optional.ofNullable(blocksStorage.get(key));
    }

    @Override
    protected void storeAggregatedBlocks(int key, String blockData) {
        blocksStorage.put(key, blockData);
    }

    @Override
    public boolean isAggregatedBlockAvailable(int key) {
        return blocksStorage.containsKey(key);
    }

    @Override
    public void disconnect() {}
}
