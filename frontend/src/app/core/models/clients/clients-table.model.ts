export class ClientsTableModel {
    page: number;
    company_name: string;
    name: string;
    email: string;
    phone: string;
    document_type: string;
    document: string;

    constructor(page: number, company_name: string, name: string, email: string, phone: string, document_type: string, document: string) {
        this.page = page;
        this.company_name = company_name;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.document_type = document_type;
        this.document = document;
    }
}