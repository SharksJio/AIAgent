package com.sharksjio.aiagent.core

/**
 * Base interface for all AI agents
 */
interface AIAgent {
    val config: AgentConfig
    
    suspend fun processRequest(request: AgentRequest): AgentResponse
    fun canHandle(topic: TopicCategory): Boolean
    fun isAvailable(): Boolean
}

/**
 * Abstract base class for AI agents providing common functionality
 */
abstract class BaseAIAgent(override val config: AgentConfig) : AIAgent {
    
    override fun canHandle(topic: TopicCategory): Boolean {
        return config.supportedTopics.contains(topic) && config.isEnabled
    }
    
    override fun isAvailable(): Boolean {
        return config.isEnabled
    }
    
    protected fun createResponse(
        requestId: String,
        content: String,
        confidence: Double,
        topic: TopicCategory,
        startTime: Long
    ): AgentResponse {
        return AgentResponse(
            id = requestId,
            agentId = config.id,
            content = content,
            confidence = confidence,
            topic = topic,
            processingTime = System.currentTimeMillis() - startTime,
            metadata = mapOf("agentName" to config.name, "agentType" to config.type)
        )
    }
}