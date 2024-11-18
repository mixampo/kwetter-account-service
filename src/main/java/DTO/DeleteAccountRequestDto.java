package DTO;

import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class DeleteAccountRequestDto {
    private String userId;
    private int accountId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public DeleteAccountRequestDto(String postId, int accountId) {
        this.userId = postId;
        this.accountId = accountId;
    }
}
