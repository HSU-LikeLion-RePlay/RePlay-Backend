# 커밋 및 브랜치, 이슈 규칙 (Commit, Branch, and Issue Convention)

## ✉️ 커밋 태그 설명

| 태그 이름     | 설명 |
|--------------| --- |
| **[chore]**  | 코드 수정, 내부 파일 수정 |
| **[feat]**   | 새로운 기능 구현 |
| **[add]**    | FEAT 이외의 부수적인 코드 추가, 라이브러리 추가, 새로운 파일 생성 |
| **[hotfix]** | issue나 QA에서 급한 버그 수정에 사용 |
| **[fix]**    | 버그, 오류 해결 |
| **[del]**    | 쓸모 없는 코드 삭제 |
| **[docs]**   | README나 WIKI 등의 문서 개정 |
| **[correct]**| 주로 문법의 오류나 타입의 변경, 이름 변경에 사용 |
| **[move]**   | 프로젝트 내 파일이나 코드의 이동 |
| **[rename]** | 파일 이름 변경이 있을 때 사용 |
| **[improve]**| 향상이 있을 때 사용 |
| **[refactor]** | 전면 수정이 있을 때 사용 |
| **[test]**   | 테스트 코드 추가 시 사용 |
| **[ci]**     | CI 설정 파일 및 스크립트 변경 (예: GitHub Actions, CircleCI 등) |
| **[perf]**   | 성능을 향상시키는 코드 변경 |
| **[build]**  | 빌드 시스템 또는 외부 종속성에 영향을 미치는 변경 사항 (예: gulp, broccoli, npm) |
| **[revert]** | 이전 커밋 되돌리기 |
| **[security]** | 보안 관련 변경 사항 |

<br>

## 📌 커밋 규칙

1. **마이크로 커밋 (Micro Commit)** ✨
   - 작은 단위로 자주 커밋하기!

2. **이해하기 쉽게 작성**
   - 커밋 메시지는 명확하고 이해하기 쉽게 작성하기

3. **커밋 형식**
   - 태그. 해당 기능 설명 `예: feat. 로그인 기능 추가`

4. **깃모지 사용**
   - 가독성을 높이기 위해 깃모지 사용하기

5. **테스트 통과 시, PR 요청**
   - 모든 테스트를 통과한 후 Pull Request 요청하기

<br><br>
## 🌿 브랜치 규칙

1. **메인 브랜치 (main)**
   - 항상 배포 가능한 상태를 유지하기!
   - 직접 커밋하지 않기 🔥

2. **개발 브랜치 (develop)**
   - 다음 배포 버전 준비
   - 새로운 기능과 버그 수정을 위한 기본 브랜치

3. **기능 브랜치 (feature)**
   - 새로운 기능을 개발 시 사용하는 브랜치
   - 네이밍 규칙: `feature/기능-이름`

4. **버그 수정 브랜치 (fix)**
   - 버그를 수정할 때 사용
   - 네이밍 규칙: `fix/버그-이름`

5. **브랜치 병합**
   - PR(Pull Request)을 통해서만 병합하기
   - 테스트 및 코드 리뷰 필수 !
   - 모든 테스트를 통과한 브랜치는 병합 후, 삭제하기

<br><br>

## 🛠️ 이슈 규칙

1. **이슈 제목**
   - 간결하고 명확하게 작성
   - 예: `로그인 페이지 버그 수정`

2. **이슈 내용**
   - 버그 재현 방법, 기대 결과, 실제 결과, 스크린샷 등을 포함하여 상세히 작성

3. **라벨 사용**
   - 이슈 유형에 따라 적절한 라벨 사용

4. **이슈 할당**
   - 담당자를 지정하기

5. **이슈 닫기**
   - 해당 이슈가 해결되면 PR과 함께 이슈 닫기
   - 이슈 닫기 메시지를 통해 해결 방법 간략히 설명
