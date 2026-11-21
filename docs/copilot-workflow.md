# Copilot CLI workflow

This is a lightweight starting point for the installed Copilot CLI. Recheck
`copilot --help`, `copilot help permissions` and `copilot instruction list`
when the CLI or project changes. `AGENTS.md` is shared project guidance, not a
security boundary. No custom agents, hooks, skills, MCP servers, model defaults
or permission rules are added by this setup.

## Everyday work

1. In the intended checkout, run `copilot --name "feature-short-name"`. Give
   the session a concrete goal, scope and acceptance criteria. One session is
   normally enough; use `/session rename <name>` to rename it and
   `copilot --resume "feature-short-name"` to resume it.
2. Use `/permissions show` and `/permissions manual` to check and select
   manual mode. The CLI's documented default is manual, but user settings,
   organization policy, saved approvals, launch flags and resumed sessions can
   affect behavior. Review each consequential request and choose one-time
   approval. `/reset-allowed-tools` also clears saved approvals for the current
   location; use it deliberately, not as an automatic setup step.
3. Use `/model` to choose a model for the session; the existing default is not
   changed. A local model is optional and must already be available or be
   configured separately. A local model alone does not ensure offline
   operation: tools, MCP servers, telemetry and other services may use the
   network. The CLI documents `COPILOT_OFFLINE=true` with a local provider to
   disable its network features; this setup has not tested an offline session,
   and external processes require separate scrutiny.
4. Ask for focused verification and `/review` when useful; inspect `/diff`
   and the actual Git diff yourself. Review findings are advice, not a gate.
   The demo process, if running, serves the checkout from which it was started;
   do not assume it reflects a different worktree. Respect the instruction not
   to start or stop it, and report when UI verification is unavailable.
5. Before ending or handing off, summarize the goal, branch and path, changed
   files, decisions, checks and results, known risks, and next step. Independent
   sessions do not automatically share conversations; explicitly pass this
   summary and inspect the actual files and Git status.

Use iTerm2 panes or windows if helpful, labeled with the session name and
checkout/branch. They are optional. Delegate a bounded specialist review or
open another session only when it adds value; don't assume a fixed team.

## Parallel feature edits

The following Git operations are **human-controlled**. Do not ask the agent to
perform them under the current project instructions. Begin from a clean,
reviewed integration branch; `master` is the current branch in this checkout,
but verify your intended base first. Worktrees share Git history and refs, not
uncommitted files or conversations. Commit reviewed setup changes first if new
worktrees need them; never commit secrets or generated build output.

For each independent feature, from the main checkout:

```sh
git status --short --branch
git worktree list
git worktree add -b feature/example ../crudui-example master
cd ../crudui-example
copilot --name "example-implementation"
```

Choose unique branch and directory names. Start separate sessions in separate
worktrees; avoid competing edits to one checkout. When finished, review
`git status --short`, `git diff`, and the staged diff in each worktree; run
appropriate checks and commit only changes you intend to integrate. From the
integration checkout, confirm it is clean and on the intended base, review
`git diff master...feature/example`, then merge the feature yourself. Resolve
conflicts deliberately, rerun relevant checks, and review the integrated
result. Worktrees reduce edit collisions, not integration conflicts.

After confirming the feature is integrated and its worktree has no needed
uncommitted or untracked files, leave that directory and run from the main
checkout:

```sh
git worktree remove ../crudui-example
git branch -d feature/example
git worktree list
```

Do not force removal or deletion to bypass warnings; investigate first. If
using one checkout instead, serialize edits, hand off status and diffs between
sessions, and avoid concurrent writes; isolation and conflict prevention are
weaker.

## Improving the guidance

When a repeatable lesson is supported by project evidence, propose the
smallest update to `AGENTS.md` if broadly applicable, or to a focused existing
document if specialized. Include the evidence and have a human review it.
Periodically remove stale or redundant guidance; do not maintain automatic
lesson logs or put mutually exclusive roles into global instructions.

## Permissions and limits

Do not use blanket approval flags or broad persistent allow rules as a
shortcut. Manual mode can still allow commands the CLI classifies as read-only
or previously approved requests. Shell commands can write files or invoke
network-capable programs; file-write rules do not constrain all shell behavior,
and shell permission patterns can cover more subcommands than expected. A
specialist role or instruction saying "read-only" is not isolation. Inspect
the actual request, the environment and saved approvals; use an independently
verified sandbox when stronger isolation is required.
