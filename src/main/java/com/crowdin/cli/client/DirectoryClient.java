package com.crowdin.cli.client;

import com.crowdin.client.CrowdinRequestBuilder;
import com.crowdin.client.api.DirectoriesApi;
import com.crowdin.common.Settings;
import com.crowdin.common.models.Directory;
import com.crowdin.common.models.Pageable;
import com.crowdin.common.response.Page;
import com.crowdin.util.PaginationUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DirectoryClient extends Client {
    private static Map<Pair<Long, String>, Directory> cache = new HashMap<>();
    private DirectoriesApi api = new DirectoriesApi(settings);

    public DirectoryClient(Settings settings) {
        super(settings);
    }

    public Optional<Directory> getProjectBranchByName(Long projectId, String name) {


        Pair<Long, String> key = Pair.of(projectId, name);
        if (cache.containsKey(key)) {
            return Optional.ofNullable(cache.get(key));
        }

        CrowdinRequestBuilder<Page<Directory>> directoriesApi = api.getProjectDirectories(projectId.toString(), Pageable.unpaged());
        List<Directory> directories = PaginationUtil.unpaged(directoriesApi);
        Directory directory = directories
                .stream()
                .filter(d -> d.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
        cache.put(key, directory);
        return Optional.ofNullable(directory);
    }
}
