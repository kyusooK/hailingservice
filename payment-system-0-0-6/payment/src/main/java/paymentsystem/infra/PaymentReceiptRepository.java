package paymentsystem.infra;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import paymentsystem.domain.*;

@RepositoryRestResource(
    collectionResourceRel = "paymentReceipts",
    path = "paymentReceipts"
)
public interface PaymentReceiptRepository
    extends PagingAndSortingRepository<PaymentReceipt, Long> {}
