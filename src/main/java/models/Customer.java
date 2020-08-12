package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer {

        
        @JsonProperty("_id") private String id;
        @JsonProperty("_rev") private String revision;
        private String name;
        
        
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

        public void setName(String s) {
                name = s;
        }
        
        public String getName() {
                return name;
        }
}
