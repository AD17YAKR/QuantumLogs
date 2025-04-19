# QuantumLogger

QuantumLogger is a plug-and-play intelligent logging and observability tool for Spring Boot applications. It captures
the first API hit per route, logs detailed request and response metadata in structured markdown files, and, in the
future, will utilize Generative AI (ChatGPT/Gemini) to summarize daily logs and diagnose errors with smart
recommendations.

## 🧩 Core Concept

QuantumLogger is not just a logger — it's a developer productivity booster that focuses on:

- ✅ **Low-noise, high-signal logs**
- ✅ **Intelligent design via annotations**
- ✅ **Async logging to avoid performance cost**
- ✅ **AI-powered log review and insights**
- ✅ **Markdown-based, dev-friendly logs**

## 🛠️ Core Features

### 1. One-Time Logging Per Route

- Logs only the first request to each unique route per session or day.
- Avoids redundant logging.
- Helps understand how the API behaves under normal usage.

### 2. Annotation-Based Setup (Filter-Powered)

- Use `@EnableQuantumLog` to activate QuantumLogger in any Spring Boot app.
- Behind the scenes, this annotation auto-registers a Spring OncePerRequestFilter that captures every HTTP request and
  response.
- Optional: Use `@ExcludeQuantumLog` to skip specific routes (via pattern match or future support through interceptors).

### 3. Detailed Request & Response Logging

Captured metadata includes:

- ✅ **Request:** Method, Path, Params, Body, Headers, Cookies
- ✅ **Response:** Status Code, Headers, Body (if applicable)

### 4. Asynchronous Logging

- Logging operations run on a separate thread pool to avoid blocking the main flow.
- Ideal for production systems with high throughput.

### 5. Structured Daily Markdown Logs

Logs are stored in a structured directory:

```plaintext
/quantumLog/
  └── 04/
      └── 20250405.md
```

- Human-readable logs perfect for developers, auditors, and reviewers.

## 🔮 Future Features

### 6. AI Summarization of Logs

- At the end of the day, send the `.md` log file to a GenAI model.
- AI generates a markdown summary that highlights:
  - ⚠️ API performance issues
  - 🔐 Security flags (e.g., large tokens, unsecured routes)
  - 💡 Optimization suggestions (e.g., add caching, trim headers)

### 7. Error Logging + AI Debugging

QuantumLogger will also:

- Capture runtime errors/exceptions.
- Log stack trace, context, and environment.
- Use GenAI to analyze and generate:
  - Root cause hypotheses
  - Fix suggestions
  - Security implications

> **Basically:** "Your logs, debugged by AI."

## 🌍 Real-World Use Cases

| **Scenario**            | **How QuantumLogger Helps**                    |
| ----------------------- | ---------------------------------------------- |
| **Staging New APIs**    | Logs first hits to validate contracts          |
| **Auditing Production** | One-time logs give visibility without overload |
| **Debugging**           | Errors are captured and interpreted by GenAI   |
| **Performance Tuning**  | AI can highlight slow or heavy endpoints       |
| **Documentation**       | First-hit logs serve as live API examples      |

## 🧪 Tech Stack

| **Layer**           | **Technology**                                |
| ------------------- | --------------------------------------------- |
| **Core Framework**  | Java + Spring Boot                            |
| **Async Execution** | `@Async` + ThreadPoolExecutor                 |
| **File Output**     | Markdown via FileWriter / BufferedWriter      |
| Core Interception   | Spring OncePerRequestFilter                   |
| Body Capture        | Request/Response wrappers (`ContentCaching*`) |
| **Scheduled Tasks** | Spring `@Scheduled` for daily summaries       |
| **AI Integration**  | OpenAI/Gemini APIs (via REST or SDK)          |

## 🧠 Why This Project Is Smart

| **Reason**              | **Impact**                                            |
| ----------------------- | ----------------------------------------------------- |
| **Plug-and-play**       | Makes it adoption-friendly for teams                  |
| **Annotation-driven**   | Devs can configure without changing code              |
| **Asynchronous**        | Performance-conscious design                          |
| **Daily Markdown Logs** | Clean, human-readable logs                            |
| **GenAI Insights**      | AI-powered observability with debugging tips          |
| **Extendable**          | Can be enhanced for dashboards, alerts, masking, etc. |
