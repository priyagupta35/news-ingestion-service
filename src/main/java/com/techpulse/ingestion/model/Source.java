package com.techpulse.ingestion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
//import sun.jvm.hotspot.gc.shared.Generation;


    @Entity
    @Table(name="sources")
    public class Source{

        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        private int id;

        @Column(name="name",nullable=false)
        private String name;

        @Column(name="website_url")
        private String websiteUrl;

        @Column(name="country")
        private String country;

        public Source() {}

        public Source(String name,String websiteUrl,String country)
        {
            this.name=name;
            this.websiteUrl=websiteUrl;
            this.country=country;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWebsiteUrl() {
            return websiteUrl;
        }

        public void setWebsiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

    }

    

