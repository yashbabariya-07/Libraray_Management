package com.lbm.entity;

import com.lbm.Enum.Days;
import com.lbm.entity.core.BaseEntity;
import com.lbm.entity.core.LocationData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import java.util.List;

@Document(collection = "library_list")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Library extends BaseEntity {

    @Indexed
    private String name;

    @Indexed
    private String uniqueId;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private LocationData location;

    private String openTime;

    private String closeTime;

    private List<Days> openingDay;

    @DocumentReference(collection = "users", lazy = true)
    private User user;

    @DocumentReference(collection = "books", lazy = false)
    private List<Book> books;
}
