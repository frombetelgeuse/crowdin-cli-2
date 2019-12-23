package com.crowdin.cli.client;

import com.crowdin.client.CrowdinRequestBuilder;
import com.crowdin.client.api.LanguagesApi;
import com.crowdin.common.Settings;
import com.crowdin.common.models.Language;
import com.crowdin.common.models.Project;
import com.crowdin.common.response.Page;
import com.crowdin.util.PaginationUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LanguagesClient extends Client {

    public LanguagesClient(Settings settings) {
        super(settings);
    }

    public List<Language> getProjectLanguages(Project project) {
        Set<String> targetLanguageIds = new HashSet<>(project.getTargetLanguageIds());
        return getAllSupportedLanguages().stream()
                .filter(language -> targetLanguageIds.contains(language.getId()))
                .collect(Collectors.toList());
    }

    public List<Language> getAllSupportedLanguages() {
        CrowdinRequestBuilder<Page<Language>> getLanguages = new LanguagesApi(settings).getLanguages(null);
        return PaginationUtil.unpaged(getLanguages, 500);
    }
}
