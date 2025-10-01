package com.hawamoni.app.moni.service;

import com.hawamoni.app.moni.dto.DepositStatus;
import com.hawamoni.app.moni.dto.GroupDTO;
import com.hawamoni.app.moni.dto.WithdrawalRequestDTO;
import com.hawamoni.app.moni.dto.WithdrawalRequestStatus;
import com.hawamoni.app.moni.exceptions.GroupDataNotFound;
import com.hawamoni.app.moni.exceptions.UserDataNotFound;
import com.hawamoni.app.moni.generator.QRCodeGenerator;
import com.hawamoni.app.moni.mappers.GroupMapper;
import com.hawamoni.app.moni.mappers.WithdrawalRequestMapper;
import com.hawamoni.app.moni.model.GroupModel;
import com.hawamoni.app.moni.model.UserModel;
import com.hawamoni.app.moni.model.WithdrawalRequestModel;
import com.hawamoni.app.moni.repository.GroupRepository;
import com.hawamoni.app.moni.repository.UserRepository;
import com.hawamoni.app.moni.repository.WithdrawalRequestRepository;
import com.hawamoni.app.moni.request.ApprovalRequest;
import com.hawamoni.app.moni.request.DepositRequest;
import com.hawamoni.app.moni.request.ExecutorRequest;
import com.hawamoni.app.moni.request.WithdrawalRequest;
import com.hawamoni.app.moni.response.GroupResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class GroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WithdrawalRequestRepository withdrawalRequestRepository;

    @Autowired
    private WithdrawalRequestMapper  withdrawalRequestMapper;


    public GroupResponse createGroup(GroupDTO groupDTO, String token) {
        GroupResponse groupResponse = null;
        if(!Objects.isNull(groupDTO)) {
            GroupModel groupModel = groupMapper.convertToModel(groupDTO);

            UserModel userModel = userRepository.
                    findByEmail(jwtService.extractEmail(token))
                            .orElseThrow(() -> new UserDataNotFound("user not found"));

            groupModel.setUserModel(userModel);

            groupRepository.save(groupModel);
            log.info("group model data added to database");
            groupResponse = new GroupResponse(groupModel.getGroupId(),userModel.getId(),
                    "Group successfully created.");
        }
        return groupResponse;
    }

    public GroupDTO getGroupByID(Long id) {
        return groupRepository.findById(id)
                .map(groupMapper::convertToDTO)
                .orElseThrow(() -> new GroupDataNotFound(String.format("group data with id: %d not found",id)));
    }

    public Map<String, Object> makeDeposit(DepositRequest depositRequest, Long groupId) {
        Map<String,Object> data = new HashMap<>();
        try {
            //deposit configs
            if (!Objects.isNull(depositRequest)) {

                String solanaURl = "";//paymentService.generateDepositSolanaURl(depositRequest);
                String reference ="";// paymentService.getReference();
                String qrCodeUrl = QRCodeGenerator.generateQRCodeImage(solanaURl, 300, 300, "Solana Pay Deposit: " + reference);
                data.put("reference", reference);
                data.put("solanaPayUrl", solanaURl);
                data.put("qrCode", qrCodeUrl);
                data.put("status", DepositStatus.INITIATED.name());

            }
        }
        catch(Exception ex) { ex.printStackTrace(); }
        return data;

    }

    public Map<String, Object> createWithdrawalRequest(WithdrawalRequest withdrawalRequest, Long groupId) {
        Map<String,Object> data = new HashMap<>();
        if(!Objects.isNull(withdrawalRequest)) {

            GroupDTO groupDTO = getGroupByID(groupId);

            if(!Objects.isNull(groupDTO)) {
                WithdrawalRequestModel withdrawalRequestModel =
                        WithdrawalRequestModel.builder()
                                .amount(withdrawalRequest.amount())
                                .groupId(groupId)
                                .createdAt(LocalDateTime.now().toString())
                                .reason(withdrawalRequest.reason())
                                .recipientPubkey(withdrawalRequest.recipientPubkey())
                                .build();
                withdrawalRequestRepository.save(withdrawalRequestModel);
                log.info("withdrawal request saved to db...");

                data.put("requestId", withdrawalRequestModel.getId());
                data.put("status", withdrawalRequestModel.getStatus());
                data.put("approvalsCount", groupDTO.getApprovals_count());
                data.put("approvalsRequired",groupDTO.getApprovals_required());
                data.put("createdAt",withdrawalRequestModel.getCreatedAt());
            }
        }
        return data;
    }

    public WithdrawalRequestDTO getWithdrawalRequest(Long requestId) {
        return withdrawalRequestRepository.findById(requestId)
                .map(withdrawalRequestMapper::convertToDTO)
                .orElseThrow(() -> new GroupDataNotFound("Withdrawal Request Not Found"));
    }

    public Map<String, Object> approveRequest(ApprovalRequest approvalRequest, Long requestId) {
        Map<String,Object> data = new HashMap<>();

        if(!Objects.isNull(approvalRequest)) {
            WithdrawalRequestDTO withdrawalRequestDTO = getWithdrawalRequest(requestId);

            //approve Request
            //withdrawalRequestDTO.setStatus(WithdrawalRequestStatus.APPROVED);

            data.put("requestId",requestId);
            data.put("status",withdrawalRequestDTO.getStatus());
            data.put("approvalsCount", "new approval count");
            data.put("approvalsRequired", withdrawalRequestDTO.getApprovalsRequired());
            data.put("approved By", withdrawalRequestDTO.getApprovedBy());
        }
        return data;
     }

    public Map<String, Object> executeRequest(ExecutorRequest executorRequest, Long requestId) {
        Map<String,Object> data = new HashMap<>();


        if(!Objects.isNull(executorRequest)) {
            //execute request

            WithdrawalRequestDTO withdrawalRequestDTO = getRequest(requestId);

            if(!Objects.isNull(withdrawalRequestDTO)) {

                data.put("requestId", requestId);
                data.put("status", withdrawalRequestDTO.getStatus());
                data.put("txSig", null);
                data.put("amount", withdrawalRequestDTO.getAmount());
                data.put("recipient", withdrawalRequestDTO.getRecipientPubkey());
                data.put("executedAt", LocalDateTime.now().toString());
            }
        }
        return data;
    }

    public WithdrawalRequestDTO getRequest(Long requestId) {
        return getWithdrawalRequest(requestId);
    }
}
