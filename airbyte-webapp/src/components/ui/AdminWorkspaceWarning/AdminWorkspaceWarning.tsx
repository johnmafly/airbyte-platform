import { FormattedMessage, useIntl } from "react-intl";

import { Text } from "components/ui/Text";

import { useListUsers } from "core/api/cloud";
import { useCurrentUser } from "packages/cloud/services/auth/AuthService";

import styles from "./AdminWorkspaceWarning.module.scss";
import { Tooltip } from "../Tooltip";

export const AdminWorkspaceWarning = () => {
  const user = useCurrentUser();
  const workspaceUsers = useListUsers();
  const { formatMessage } = useIntl();

  if (!workspaceUsers.users.some((member) => member.userId === user.userId)) {
    return (
      <div className={styles.adminWorkspaceWarning}>
        <Tooltip
          placement="right"
          control={
            <div className={styles.adminWorkspaceWarning__pill}>
              <Text inverseColor>{formatMessage({ id: "workspace.adminWorkspaceWarning" })}</Text>
            </div>
          }
        >
          <FormattedMessage id="workspace.adminWorkspaceWarningTooltip" />
        </Tooltip>
      </div>
    );
  }

  return null;
};
