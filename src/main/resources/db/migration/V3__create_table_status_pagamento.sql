CREATE TABLE status_pagamento (
    id_status_pagamento INT AUTO_INCREMENT PRIMARY KEY,
    nome_status VARCHAR(100) NOT NULL
);

--ALTER TABLE pagamento
  --  ADD CONSTRAINT FK_pagamento_status FOREIGN KEY (id_status_pagamento)
    --REFERENCES status_pagamento(id_status_pagamento);
