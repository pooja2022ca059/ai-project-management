package com.aipm.ai_project_management.modules.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class PasswordStrengthResponse {
    
    private String strength;
    private int score;
    
    @JsonProperty("suggestions")
    private List<String> suggestions;
    
    @JsonProperty("meets_requirements")
    private boolean meetsRequirements;
    
    // Default constructor
    public PasswordStrengthResponse() {
    }
    
    // All-args constructor
    public PasswordStrengthResponse(String strength, int score, List<String> suggestions, boolean meetsRequirements) {
        this.strength = strength;
        this.score = score;
        this.suggestions = suggestions;
        this.meetsRequirements = meetsRequirements;
    }
    
    // Getters and setters
    public String getStrength() {
        return strength;
    }
    
    public void setStrength(String strength) {
        this.strength = strength;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public List<String> getSuggestions() {
        return suggestions;
    }
    
    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }
    
    public boolean isMeetsRequirements() {
        return meetsRequirements;
    }
    
    public void setMeetsRequirements(boolean meetsRequirements) {
        this.meetsRequirements = meetsRequirements;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordStrengthResponse that = (PasswordStrengthResponse) o;
        return score == that.score &&
                meetsRequirements == that.meetsRequirements &&
                Objects.equals(strength, that.strength) &&
                Objects.equals(suggestions, that.suggestions);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(strength, score, suggestions, meetsRequirements);
    }
    
    @Override
    public String toString() {
        return "PasswordStrengthResponse{" +
                "strength='" + strength + '\'' +
                ", score=" + score +
                ", suggestions=" + suggestions +
                ", meetsRequirements=" + meetsRequirements +
                '}';
    }
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String strength;
        private int score;
        private List<String> suggestions;
        private boolean meetsRequirements;
        
        public Builder strength(String strength) {
            this.strength = strength;
            return this;
        }
        
        public Builder score(int score) {
            this.score = score;
            return this;
        }
        
        public Builder suggestions(List<String> suggestions) {
            this.suggestions = suggestions;
            return this;
        }
        
        public Builder meetsRequirements(boolean meetsRequirements) {
            this.meetsRequirements = meetsRequirements;
            return this;
        }
        
        public PasswordStrengthResponse build() {
            return new PasswordStrengthResponse(strength, score, suggestions, meetsRequirements);
        }
    }
}