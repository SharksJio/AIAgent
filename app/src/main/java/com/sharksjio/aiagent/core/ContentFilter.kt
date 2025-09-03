package com.sharksjio.aiagent.core

/**
 * Content filter that analyzes and filters content based on topics and use cases
 */
class ContentFilter {
    
    private val topicKeywords = mapOf(
        TopicCategory.TECHNOLOGY to setOf("tech", "software", "ai", "computer", "programming", "code", "algorithm", "data", "system"),
        TopicCategory.SCIENCE to setOf("research", "experiment", "theory", "study", "biology", "chemistry", "physics", "scientific"),
        TopicCategory.BUSINESS to setOf("market", "company", "finance", "strategy", "revenue", "profit", "management", "enterprise"),
        TopicCategory.EDUCATION to setOf("learn", "study", "school", "university", "course", "training", "knowledge", "teaching"),
        TopicCategory.ENTERTAINMENT to setOf("movie", "music", "game", "fun", "entertainment", "show", "sport", "hobby"),
        TopicCategory.HEALTH to setOf("health", "medical", "doctor", "medicine", "fitness", "wellness", "disease", "treatment")
    )
    
    /**
     * Analyzes content and determines the most appropriate topic category
     */
    fun detectTopic(content: String): TopicCategory {
        val lowerContent = content.lowercase()
        val topicScores = mutableMapOf<TopicCategory, Int>()
        
        topicKeywords.forEach { (topic, keywords) ->
            val score = keywords.count { keyword -> lowerContent.contains(keyword) }
            topicScores[topic] = score
        }
        
        return topicScores.maxByOrNull { it.value }?.key ?: TopicCategory.GENERAL
    }
    
    /**
     * Filters content based on appropriateness and safety
     */
    fun isContentAppropriate(content: String): Boolean {
        val inappropriateKeywords = setOf("spam", "inappropriate", "offensive")
        val lowerContent = content.lowercase()
        return !inappropriateKeywords.any { lowerContent.contains(it) }
    }
    
    /**
     * Calculates relevance score for content against a specific topic
     */
    fun calculateRelevanceScore(content: String, topic: TopicCategory): Double {
        val keywords = topicKeywords[topic] ?: emptySet()
        val lowerContent = content.lowercase()
        val matches = keywords.count { lowerContent.contains(it) }
        return if (keywords.isNotEmpty()) matches.toDouble() / keywords.size else 0.0
    }
}