package com.sharksjio.aiagent.core

/**
 * Represents different types of AI agents that can be integrated
 */
enum class AgentType {
    TEXT_PROCESSOR,
    CONTENT_ANALYZER,
    QUESTION_ANSWERER,
    CONVERSATION_HANDLER,
    CUSTOM
}

/**
 * Topics that agents can be filtered by
 */
enum class TopicCategory {
    TECHNOLOGY,
    SCIENCE,
    BUSINESS,
    EDUCATION,
    ENTERTAINMENT,
    HEALTH,
    GENERAL
}

/**
 * Request object for agent interactions
 */
data class AgentRequest(
    val id: String,
    val content: String,
    val topic: TopicCategory,
    val metadata: Map<String, Any> = emptyMap()
)

/**
 * Response object from agent interactions
 */
data class AgentResponse(
    val id: String,
    val agentId: String,
    val content: String,
    val confidence: Double,
    val topic: TopicCategory,
    val processingTime: Long,
    val metadata: Map<String, Any> = emptyMap()
)

/**
 * Configuration for an agent
 */
data class AgentConfig(
    val id: String,
    val name: String,
    val type: AgentType,
    val supportedTopics: Set<TopicCategory>,
    val priority: Int,
    val isEnabled: Boolean = true,
    val endpoint: String? = null,
    val apiKey: String? = null,
    val customProperties: Map<String, Any> = emptyMap()
)