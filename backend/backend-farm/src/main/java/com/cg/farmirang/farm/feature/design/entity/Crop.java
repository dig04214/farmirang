package com.cg.farmirang.farm.feature.design.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class Crop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crop_id")
    private Integer id;

    private String name;
    private String time;

    public static CropBuilder builder() {
        return new CropBuilder();
    }

    public static class CropBuilder {
        private Integer id;
        private String name;
        private String time;

        CropBuilder() {
        }

        public CropBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public CropBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CropBuilder time(String time) {
            this.time = time;
            return this;
        }

        public Crop build() {
            return new Crop(this.id, this.name, this.time);
        }

        public String toString() {
            return "Crop.CropBuilder(id=" + this.id + ", name=" + this.name + ", time=" + this.time + ")";
        }
    }
}
