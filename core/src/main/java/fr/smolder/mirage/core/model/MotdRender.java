package fr.smolder.mirage.core.model;

import java.util.List;

public record MotdRender(
        RenderState state,
        String modernJson,
        String fallbackText,
        List<String> missingTileHashes,
        int columns,
        List<SkinData> orderedSkins
) {
    public MotdRender {
        missingTileHashes = List.copyOf(missingTileHashes);
        orderedSkins = List.copyOf(orderedSkins);
    }

    public static MotdRender ready(String modernJson, String fallbackText, int columns, List<SkinData> orderedSkins) {
        return new MotdRender(RenderState.READY, modernJson, fallbackText, List.of(), columns, orderedSkins);
    }

    public static MotdRender loading(String fallbackText, List<String> missingTileHashes) {
        return new MotdRender(RenderState.LOADING, "", fallbackText, missingTileHashes, 0, List.of());
    }

    public enum RenderState {
        READY,
        LOADING
    }
}
