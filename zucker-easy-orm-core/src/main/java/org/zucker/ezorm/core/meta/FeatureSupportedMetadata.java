package org.zucker.ezorm.core.meta;

import org.zucker.ezorm.core.CastUtil;
import org.zucker.ezorm.core.FeatureId;
import org.zucker.ezorm.core.FeatureType;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface FeatureSupportedMetadata {

    Map<String, Feature> getFeatures();

    void addFeature(Feature feature);

    default List<Feature> getFeatureList() {
        return ofNullable(getFeatures())
                .map(Map::values)
                .<List<Feature>>map(ArrayList::new)
                .orElseGet(Collections::emptyList);
    }

    default <T extends Feature> List<T> getFeatures(FeatureType type) {
        return ofNullable(getFeatures())
                .map(features -> features.values()
                        .stream()
                        .filter(feature -> feature.getType().equals(type))
                        .map(CastUtil::<T>cast)
                        .collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);

    }

    default <T extends Feature> Optional<T> getFeature(FeatureId<T> id) {
        return getFeature(id.getId());
    }

    default <T extends Feature> Optional<T> getFeatureNow(FeatureId<T> id) {
        return getFeatureNow(id.getId());
    }

    default <T extends Feature> Optional<T> findFeature(FeatureId<T> id) {
        return findFeature(id.getId());
    }

    default <T extends Feature> T findFeatureNow(FeatureId<T> id) {
        return this.findFeatureNow(id.getId());
    }

    default <T extends Feature> Optional<T> findFeature(String id) {
        return Optional.ofNullable(findFeatureOrElse(id, null));
    }


    default <T extends Feature> T findFeatureOrElse(FeatureId<T> id, Supplier<T> orElse) {
        return findFeatureOrElse(id.getId(), orElse);
    }

    default <T extends Feature> T findFeatureOrElse(String id, Supplier<T> orElse) {
        return getFeatureOrElse(id, orElse);
    }

    default <T extends Feature> T findFeatureNow(String id) {
        return this.findFeatureOrElse(id, () -> {
            throw new UnsupportedOperationException("unsupported feature " + id);
        });
    }

    default <T extends Feature> T getFeatureNow(String id) {
        return this.getFeatureOrElse(id, () -> {
            throw new UnsupportedOperationException("unsupported feature " + id);
        });
    }

    default <T extends Feature> T getFeatureOrElse(String id, Supplier<T> orElse) {
        Map<String, Feature> featureMap = getFeatures();
        System.out.println(featureMap + "==Map");
        Feature feature = featureMap.get(id);
        if (feature != null) {
            return CastUtil.cast(feature);
        }
        return orElse == null ? null : orElse.get();
    }

    default <T extends Feature> Optional<T> getFeature(String id) {
        return ofNullable(getFeatures())
                .map(feature -> feature.get(id))
                .map(CastUtil::cast);
    }

    default <T extends Feature> Optional<T> getFeature(Feature target) {
        return ofNullable(getFeatures())
                .map(feature -> feature.get(target.getId()))
                .map(CastUtil::cast);
    }

    default boolean supportFeature(String id) {
        return getFeature(id).isPresent();
    }

    default boolean supportFeature(Feature feature) {
        return getFeature(feature.getId()).isPresent();
    }
}
