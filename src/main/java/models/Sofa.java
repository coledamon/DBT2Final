package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sofa {

        
        @JsonProperty("_id") private String id;
        @JsonProperty("_rev") private String revision;
        private String color;
        
        
        public String getId() {
                return id;
        }

        public void setId(String s) {
                id = s;
        }

        public String getRevision() {
                return revision;
        }

        
        public void setRevision(String s) {
                revision = s;
        }

        public void setColor(String s) {
                color = s;
        }
        
        public String getColor() {
                return color;
        }
}
