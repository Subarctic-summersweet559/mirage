package fr.smolder.mirage.core.text;

import fr.smolder.mirage.core.model.SkinData;
import fr.smolder.mirage.core.model.SlicedImage;
import fr.smolder.mirage.core.model.TileSkin;

import java.util.Map;

public final class ObjectComponentJsonGenerator {
    private static final String EMPTY_PROFILE_ID = "00000000-0000-0000-0000-000000000000";

    public String generate(SlicedImage slicedImage, Map<String, SkinData> skinDataByHash) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"text\":\"\",\"extra\":[");

        boolean first = true;
        int index = 0;
        for (TileSkin tile : slicedImage.tiles()) {
            if (!first) {
                builder.append(',');
            }
            first = false;

            SkinData skinData = skinDataByHash.get(tile.tileHash());
            if (skinData == null) {
                throw new IllegalArgumentException("Missing skin data for tile hash " + tile.tileHash());
            }

            builder.append(objectComponent(skinData, tile.hat()));
            index++;

            if (index % slicedImage.columns() == 0 && index < slicedImage.tiles().size()) {
                builder.append(",{\"text\":\"\\n\"}");
            }
        }

        builder.append("]}");
        return builder.toString();
    }

    // This mirrors the draft spec closely enough for initial wiring and can be refined
    // once the final 1.21.11 object component behavior is validated end-to-end.
    private static String objectComponent(SkinData skinData, boolean hat) {
        return """
                {"type":"object","player":{"id":"%s","properties":[{"name":"textures","value":"%s","signature":"%s"}]},"hat":%s}
                """.formatted(
                EMPTY_PROFILE_ID,
                escape(skinData.textureBase64()),
                escape(skinData.signature()),
                hat
        ).trim();
    }

    private static String escape(String input) {
        return input
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}
