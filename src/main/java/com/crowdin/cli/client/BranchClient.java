package com.crowdin.cli.client;

import com.crowdin.client.CrowdinRequestBuilder;
import com.crowdin.client.api.BranchesApi;
import com.crowdin.common.Settings;
import com.crowdin.common.models.Branch;
import com.crowdin.common.response.Page;
import com.crowdin.util.PaginationUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BranchClient extends Client {

    private static Map<Pair<Long, String>, Branch> cache = new HashMap<>();

    public BranchClient(Settings settings) {
        super(settings);
    }

    public Optional<Branch> getProjectBranchByName(Long projectId, String name) {
        Pair<Long, String> key = Pair.of(projectId, name);
        if (cache.containsKey(key)) {
            return Optional.ofNullable(cache.get(key));
        }

        Branch branch = getAllSupportedBranches(projectId.toString()).stream()
                .filter(b -> b.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        cache.put(key, branch);
        return Optional.ofNullable(branch);
    }

    public List<Branch> getAllSupportedBranches(String projectId) {
        CrowdinRequestBuilder<Page<Branch>> branches = new BranchesApi(settings).getBranches(projectId, null);
        return PaginationUtil.unpaged(branches);
    }
}
