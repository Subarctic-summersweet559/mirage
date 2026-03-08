package fr.smolder.mirage.core.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ConfigLoader {
    public MirageConfig load(Path path) throws IOException {
        if (Files.notExists(path)) {
            return MirageConfig.defaults();
        }

        ConfigurationLoader<CommentedConfigurationNode> loader = YamlConfigurationLoader.builder()
                .path(path)
                .build();
        CommentedConfigurationNode root = loader.load();

        CommentedConfigurationNode settingsNode = root.node("settings");
        MirageConfig.Settings settings = new MirageConfig.Settings(
                settingsNode.node("mineskin_api_key").getString(""),
                settingsNode.node("database_type").getString("sqlite"),
                settingsNode.node("minimum_modern_protocol").getInt(769),
                settingsNode.node("mineskin_skin_visibility").getString("unlisted")
        );

        Map<String, MirageConfig.ImageEntry> images = new LinkedHashMap<>();
        for (var child : root.node("images").childrenMap().entrySet()) {
            String key = String.valueOf(child.getKey());
            CommentedConfigurationNode node = child.getValue();
            List<MirageConfig.LineStyle> lineStyles = new ArrayList<>();
            for (CommentedConfigurationNode styleNode : node.node("line_styles").childrenList()) {
                lineStyles.add(new MirageConfig.LineStyle(
                        styleNode.node("text_color").getString(),
                        styleNode.node("shadow_color").getString()
                ));
            }
            images.put(key, new MirageConfig.ImageEntry(
                    node.node("file").getString(""),
                    node.node("text_color").getString("#FFFFFF"),
                    node.node("shadow_color").getString("#FFFFFFFF"),
                    lineStyles
            ));
        }

        Map<String, MirageConfig.MotdEntry> motds = new LinkedHashMap<>();
        for (var child : root.node("motd").childrenMap().entrySet()) {
            String key = String.valueOf(child.getKey());
            CommentedConfigurationNode node = child.getValue();
            motds.put(key, new MirageConfig.MotdEntry(
                    node.node("type").getString("image"),
                    node.node("target_image").getString(""),
                    node.node("fallback_text").getString("Loading...")
            ));
        }

        return new MirageConfig(
                settings,
                images.isEmpty() ? MirageConfig.defaults().images() : images,
                motds.isEmpty() ? MirageConfig.defaults().motds() : motds
        );
    }
}
