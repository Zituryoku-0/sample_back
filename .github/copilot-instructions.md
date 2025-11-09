# Copilot Code Review – Repository Guidelines (sample_back)

本リポジトリは **Java / Spring Boot** を用いた REST バックエンドです。ビルドは **Gradle**、DB は **PostgreSQL**（Docker/Compose）を前提にしています。  
Copilot は以下の指針に基づいてプルリクエスト（PR）をレビューしてください。

> 💬 **レビュー言語指定**  
> すべてのコメント・提案・指摘は **日本語** で記載してください。  
> ただし、コード例や関数名・技術用語（Spring Boot, Javaなど）は英語のままで構いません。  
> 丁寧でわかりやすい文体で説明してください。  
> 指摘の際は「なぜそうすべきか」の理由も添えてください。
---

## Goals（レビューのゴール）
- バグや設計上の不整合の **早期検出**
- **セキュリティ・パフォーマンス・可読性** の改善提案
- **テスト（単体/結合）** の充実度向上
- **DB変更（マイグレーション）** の安全性確認
- **API仕様**（リクエスト/レスポンス、ステータスコード）の一貫性確保

## Non-Goals（対象外）
- 個人のコーディング好みの強制（明確な規約に反しない限り）
- 大規模リファクタの提案（別PRで提案する旨をコメント）

---

## Review Focus（観点）

### 1) セキュリティ
- 入力検証：`jakarta.validation`（例：`@NotNull`, `@Size`, `@Email`, `@Pattern`）をDTOで適用しているか。
- SQLインジェクション対策：JPA/Query の **パラメータバインド** を使用（文字列連結は禁止）。
- 認可/認証：セキュリティ設定（Spring Security）で **公開/保護エンドポイント** の境界が明確か。
- 機微情報のログ出力禁止（パスワード、トークン、個人情報）。
- 外部連携のタイムアウト/リトライ/サーキットブレーカ設定があるか。

### 2) 正しさ/設計
- **ドメインモデルとDTOの分離**：`@RestController` は **DTO** を入出力に用い、**エンティティを外部公開しない**。
- 例外処理：`@ControllerAdvice` + `@ExceptionHandler` で一貫したエラーレスポンス（`traceId` 等）を返す。
- 取引境界：`@Transactional` の粒度が適切か（読取系は `readOnly = true`）。
- N+1 クエリ回避（`fetch join` / `@EntityGraph` の活用）。
- 日時の扱い：**UTC保存**、APIは ISO-8601（`Z` 付き）で返却すること。

### 3) パフォーマンス
- 計算量の悪化（O(n^2) など）や **不要な同期処理/ブロッキング** を避ける。
- キャッシュが有効な箇所（読み取り多）に `@Cacheable` 等の検討。
- DB インデックス：検索条件/外部キーに妥当なインデックスがあるか。
- 大量データ取得時はページング（`page`,`size`,`sort`）必須。

### 4) API仕様
- **HTTPメソッド/ステータス** を正しく使用（POST=201、PUT/PATCH=200/204、DELETE=204 等）。
- **エンドポイント命名** は複数形のリソース指向（例：`/api/v1/users`）。
- **バリデーションエラー** のレスポンスフォーマット（フィールド/メッセージ/コード）を統一。
- **OpenAPI（Swagger）** の更新（`springdoc-openapi` 等）を忘れていないか。
- 破壊的変更は **バージョニング**（`/v1` → `/v2`）で提供。

### 5) テスト
- 新規/変更ロジックに **JUnit 5 + AssertJ** の単体テストがあるか。
- Web層は **MockMvc/WebTestClient**、DBを伴う結合は **Testcontainers**（PostgreSQL）を推奨。
- **境界値/例外系/権限違反** ケースを含める。
- テストが外部環境に依存しない（固定ポート/外部サービス固定値などを避ける）。

### 6) DB・マイグレーション
- スキーマ変更は **Flyway** のマイグレーションを追加（`VyyyymmddHHmm__desc.sql`）。
- **後方互換** を意識（段階的移行、null許容 → 非nullは既存データの埋め込みステップを挟む）。
- 破壊的変更（DROP/型変更）はローリング戦略かリリースノートで周知。
- データ修正系スクリプトは **トランザクション** と **リトライ安全性** を考慮。

### 7) ロギング/可観測性
- ログは **構造化**（JSON想定）で、レベル適切（INFO/DEBUG/ERROR）。
- 重要処理に **メトリクス**（Micrometer）・**トレース**（OpenTelemetry）を追加検討。
- 例外は cause を失わないようにラップ。

### 8) コードスタイル
- **Spotless / Checkstyle / PMD / SpotBugs** 等の静的解析にパスすること。
- **不必要な `public` / `static`**、長大メソッド、循環依存を避ける。
- Null安全（`Optional` は返却値中心、引数には多用しない）。
- Lombok 使用時は可読性を損なわない（`@Builder` 過剰乱用に注意）。

---

## Files of Interest（Copilotが特に見る場所）
- `src/main/java/**`
- `src/main/resources/**`（`application.yml` / `application-*.yml`）
- `src/test/**`
- `db/migration/**`（Flyway）
- `compose.yaml` / `Dockerfile*`（ポート/環境変数の不整合）
- Gradle: `build.gradle`, `settings.gradle`, `gradle.properties`

## Files to Ignore（ノイズ除外）
- `build/**`, `.gradle/**`, `.idea/**`
- ログ/一時ファイル、生成コード、巨大な差分（依存の自動更新など）

---

## PR ルール
- **小さめPR** を推奨（500行超は要分割検討）。
- 変更点の概要・影響範囲・ロールバック方法を **PR説明** に明記。
- 仕様変更時は **OpenAPI・README** を更新。
- CIが失敗しているPRはレビュー対象外（まず直す）。

---

## Copilotへの具体的依頼（コメントスタイル例）
- 「このDTOに `jakarta.validation` が不足。`@NotNull` と桁制限（`@Size(max=...)`）を追加してください。」
- 「このクエリは N+1 の懸念。`@EntityGraph` か `fetch join` を検討してください。」
- 「`POST /api/v1/users` は作成成功時に **201** を返し、`Location` ヘッダで新規IDを提示してください。」
- 「例外を `ControllerAdvice` で一元化し、エラーレスポンスに `traceId` を含めてください。」
- 「結合テストで Testcontainers を使い、`/users?page=1&size=20` のページング/ソートを検証してください。」
- 「Flyway の `V20251108...` に **NOT NULL + default** を追加する前に、既存データの埋め込みステップを分けてください。」

---

## Autofix（自動修正してよい範囲）
- Javadoc/コメント、typo、命名の微調整
- import順序・不要importの削除
- `final` / `private` 漏れの補完、`@Override` 付与
- テストの軽微なアサート改善（`assertEquals` → `assertThat` など）

**ただし、公開APIの入出力やDBスキーマを変える修正は提案のみ（自動変更しない）**。

---

## 環境・実行（参考）
- **アプリ**: Spring Boot / Gradle
- **DB**: PostgreSQL（Docker/Compose）
- **ポート/ENV**：`application.yml` と `compose.yaml` の整合を常に確認（例：DBホスト/ユーザー/パス、DDLモード、プール設定）。

---

## 定型チェックリスト（PR作成者向け）
- [ ] 新規/変更ロジックに **単体テスト** を追加した
- [ ] 例外とエラーレスポンスが **一貫** している
- [ ] **バリデーション**（DTO）を追加した
- [ ] API仕様（メソッド/ステータス/パス）が一貫している
- [ ] **OpenAPI** を更新した
- [ ] **Flyway** を追加/更新し、ロールバック戦略を検討した
- [ ] **N+1/インデックス** を確認した
- [ ] ログに機微情報を出していない
- [ ] CI がグリーン

---