package com.project.project.collections.service;

import com.project.project.collections.model.Collection;
import com.project.project.collections.model.CollectionHistory;
import com.project.project.collections.model.CollectionResponse;
import com.project.project.collections.repository.CollectionHistoryRepository;
import com.project.project.collections.repository.CollectionRepository;
import com.project.project.main.model.Profile;
import com.project.project.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final CollectionHistoryRepository collectionHistoryRepository;

    public List<CollectionResponse> getCollectionInfo(Profile profile) {
        var result = new ArrayList<CollectionResponse>();

        collectionRepository.findByProfile(profile).forEach(collection -> {
            BigDecimal counter = BigDecimal.ZERO;
            var donates = collectionHistoryRepository.findByCollection_id(collection.getId()).get();

            counter = donates.stream().map(CollectionHistory::getMoney).reduce(counter, BigDecimal::add);

            var percentage = counter.divide(collection.getTarget(), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).intValue();

            result.add(new CollectionResponse(collection.getId(), collection.getProfile().getUser(), collection.getTitle(), collection.getTarget(), collection.getDescription(), collection.getCreationDate(), percentage, donates));
        });
        return result;
    }

    public List<CollectionResponse> getFoundCollection(String title) {

        var result = new ArrayList<CollectionResponse>();
        collectionRepository.findAll().forEach(collection -> {
            BigDecimal counter = BigDecimal.ZERO;
            var donates = collectionHistoryRepository.findByCollection_id(collection.getId()).get();

            counter = donates.stream().map(CollectionHistory::getMoney).reduce(counter, BigDecimal::add);

            var percentage = counter.divide(collection.getTarget(), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).intValue();

            result.add(new CollectionResponse(collection.getId(), collection.getProfile().getUser(), collection.getTitle(), collection.getTarget(), collection.getDescription(), collection.getCreationDate(), percentage, donates));
        });

        return result.stream()
                .filter(collectionResponse -> collectionResponse.title().toLowerCase().contains(title.toLowerCase()))
                .toList();
    }
}
