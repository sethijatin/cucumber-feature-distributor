package cucumber.feature.distributor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Feature {

    @JsonProperty("comments")
    private ArrayList<Comment> comments;

    @JsonProperty("line")
    private String line;

    @JsonProperty("name")
    private String name;

    @JsonProperty("keyword")
    private String keyword;

    @JsonProperty("id")
    private String id;

    @JsonProperty("description")
    private String description;

    @JsonProperty("uri")
    private String uri;

    @JsonProperty("elements")
    private ArrayList<Scenario> elements;

    @JsonProperty("tags")
    private ArrayList<Tag> tags;

    public void setComments(ArrayList<Comment> comments){
        this.comments = comments;
    }

    public ArrayList<Comment> getComments(){
        return this.comments;
    }

    public void setLine (String line){
        this.line = line;
    }

    public String getLine (){
        return this.line;
    }

    public void setId (String id){
        this.id = id;
    }

    public String getId (){
        return this.id;
    }

    public void setName (String name){
        this.name = name;
    }

    public String getName (){
        return this.name;
    }

    public void setKeyword (String keyword){
        this.keyword = keyword;
    }

    public String getKeyword (){
        return this.keyword;
    }

    public void setDescription (String description){
        this.description = description;
    }

    public String getDescription (){
        return this.description;
    }

    public void setUri (String uri){
        this.uri = uri;
    }

    public String getUri (){
        return this.uri;
    }

    public void setElements (ArrayList<Scenario> elements){
        this.elements = elements;
    }

    public ArrayList<Scenario> getElements (){
        return this.elements;
    }

    public void setTags (ArrayList<Tag> tags){
        this.tags = tags;
    }

    public ArrayList<Tag> getTags (){
        return this.tags;
    }

    public static class Comment {

        @JsonProperty("line")
        private String line;

        @JsonProperty("value")
        private String value;

        public void setLine(String line){
            this.line = line;
        }

        public String getLine(){
            return this.line;
        }

        public void setValue(String value){
            this.value = value;
        }

        public String getValue(){
            return this.value;
        }
    }

    public static class Scenario {

        @JsonProperty("comments")
        private ArrayList<Comment> comments;

        @JsonProperty("line")
        private String line;

        @JsonProperty("name")
        private String name;

        @JsonProperty("keyword")
        private String keyword;

        @JsonProperty("id")
        private String id;

        @JsonProperty("description")
        private String description;

        @JsonProperty("type")
        private String type;

        @JsonProperty("examples")
        private ArrayList<Example> examples;

        @JsonProperty("steps")
        private ArrayList<Step> steps;

        @JsonProperty("tags")
        private ArrayList<Tag> tags;

        public void setComments(ArrayList<Comment> comments){
            this.comments = comments;
        }

        public ArrayList<Comment> getComments(){
            return this.comments;
        }

        public void setLine (String line){
            this.line = line;
        }

        public String getLine (){
            return this.line;
        }

        public void setId (String id){
            this.id = id;
        }

        public String getId (){
            return this.id;
        }

        public void setName (String name){
            this.name = name;
        }

        public String getName (){
            return this.name;
        }

        public void setKeyword (String keyword){
            this.keyword = keyword;
        }

        public String getKeyword (){
            return this.keyword;
        }

        public void setDescription (String description){
            this.description = description;
        }

        public String getDescription (){
            return this.description;
        }

        public void setType (String type){
            this.type = type;
        }

        public String getType (){
            return this.type;
        }

        public void setExamples (ArrayList<Example> examples){
            this.examples = examples;
        }

        public ArrayList<Example> getExamples (){
            return this.examples;
        }

        public void setTags (ArrayList<Tag> tags){
            this.tags = tags;
        }

        public ArrayList<Tag> getTags (){
            return this.tags;
        }

        public void setSteps (ArrayList<Step> steps){
            this.steps = steps;
        }

        public ArrayList<Step> getSteps (){
            return this.steps;
        }

    }

    public static class Example {

        @JsonProperty("comments")
        private ArrayList<Comment> comments;

        @JsonProperty("line")
        private String line;

        @JsonProperty("name")
        private String name;

        @JsonProperty("keyword")
        private String keyword;

        @JsonProperty("id")
        private String id;

        @JsonProperty("description")
        private String description;

        @JsonProperty("rows")
        private ArrayList<Row> rows;

        @JsonProperty("tags")
        private ArrayList<Tag> tags;

        public void setComments(ArrayList<Comment> comments){
            this.comments = comments;
        }

        public ArrayList<Comment> getComments(){
            return this.comments;
        }

        public void setLine (String line){
            this.line = line;
        }

        public String getLine (){
            return this.line;
        }

        public void setId (String id){
            this.id = id;
        }

        public String getId (){
            return this.id;
        }

        public void setName (String name){
            this.name = name;
        }

        public String getName (){
            return this.name;
        }

        public void setKeyword (String keyword){
            this.keyword = keyword;
        }

        public String getKeyword (){
            return this.keyword;
        }

        public void setDescription (String description){
            this.description = description;
        }

        public String getDescription (){
            return this.description;
        }

        public void setRows (ArrayList<Row> rows){
            this.rows = rows;
        }

        public ArrayList<Row> getRows (){
            return this.rows;
        }

        public void setTags (ArrayList<Tag> tags){
            this.tags = tags;
        }

        public ArrayList<Tag> getTags (){
            return this.tags;
        }

    }

    public static class Row {

        @JsonProperty("comments")
        private ArrayList<Comment> comments;

        @JsonProperty("line")
        private String line;

        @JsonProperty("id")
        private String id;

        @JsonProperty("cells")
        private ArrayList<String> cells;

        public void setComments(ArrayList<Comment> comments){
            this.comments = comments;
        }

        public ArrayList<Comment> getComments(){
            return this.comments;
        }

        public void setLine (String line){
            this.line = line;
        }

        public String getLine (){
            return this.line;
        }

        public void setId (String id){
            this.id = id;
        }

        public String getId (){
            return this.id;
        }

        public void setCells (ArrayList<String> cells){
            this.cells = cells;
        }

        public ArrayList<String> getCells(){
            return this.cells;
        }

    }

    public static class Step {

        @JsonProperty("comments")
        private ArrayList<Comment> comments;

        @JsonProperty("line")
        private String line;

        @JsonProperty("name")
        private String name;

        @JsonProperty("keyword")
        private String keyword;

        @JsonProperty("doc_string")
        private DocString docString;

        @JsonProperty("rows")
        private ArrayList<Row> rows;

        public void setComments(ArrayList<Comment> comments){
            this.comments = comments;
        }

        public ArrayList<Comment> getComments(){
            return this.comments;
        }

        public void setLine (String line){
            this.line = line;
        }

        public String getLine (){
            return this.line;
        }

        public void setName (String name){
            this.name = name;
        }

        public String getName (){
            return this.name;
        }

        public void setKeyword (String keyword){
            this.keyword = keyword;
        }

        public String getKeyword (){
            return this.keyword;
        }

        public void setDocString (DocString docString){
            this.docString = docString;
        }

        public DocString getDocString (){
            return this.docString;
        }

        public void setRows (ArrayList<Row> rows){
            this.rows = rows;
        }

        public ArrayList<Row> getRows (){
            return this.rows;
        }
    }

    public static class DocString {

        @JsonProperty("line")
        private String line;

        @JsonProperty("content_type")
        private String content_type;

        @JsonProperty("value")
        private String value;

        public void setLine (String line){
            this.line = line;
        }

        public String getLine (){
            return this.line;
        }

        public void setContentType (String content_type){
            this.content_type = content_type;
        }

        public String getContentType (){
            return this.content_type;
        }

        public void setValue (String value){
            this.value = value;
        }

        public String getValue (){
            return this.value;
        }
    }

    public static class Tag {

        @JsonProperty("line")
        private String line;

        @JsonProperty("name")
        private String name;

        public void setLine (String line){
            this.line = line;
        }

        public String getLine (){
            return this.line;
        }

        public void setName (String name){
            this.name = name;
        }

        public String getName (){
            return this.name;
        }
    }

}
